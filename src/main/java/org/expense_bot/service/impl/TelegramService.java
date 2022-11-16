package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.model.Request;
import org.expense_bot.sender.ExpenseBotSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramService {

    private final ExpenseBotSender botSender;

    public void editKeyboardMarkup(Request request, InlineKeyboardMarkup replyKeyboard){
        final Long userId = request.getUserId();
        sendTyping(userId);
        if(RequestHandler.hasCallBack(request)) {
            final Integer messageId = request.getUpdate().getCallbackQuery().getMessage().getMessageId();
            execute(getReplyMarkup(replyKeyboard, userId, messageId));
        }
    }

    private EditMessageReplyMarkup getReplyMarkup(InlineKeyboardMarkup replyKeyboard, Long userId, Integer messageId) {
        return EditMessageReplyMarkup.builder()
          .chatId(String.valueOf(userId))
          .replyMarkup(replyKeyboard)
          .messageId(messageId)
          .build();
    }

    public void sendMessage(Long userId, String text) {
        sendTyping(userId);
        sendMessage(userId, text, null);
    }

    public void buildDetailMessage(Request request, List<String> messages) {
        final Long userId = request.getUserId();
        sendTyping(userId);
        final Integer messageId = request.getUpdate().getCallbackQuery().getMessage().getMessageId();
        EditMessageText replyMarkup = EditMessageText.builder()
          .chatId(String.valueOf(userId))
          .text(String.join("\n",messages))
          .parseMode(ParseMode.HTML)
          .messageId(messageId)
          .build();
        execute(replyMarkup);
    }

    public void editNextMessage(Request request, String text) {
        final Long userId = request.getUserId();
        sendTyping(userId);
        if(RequestHandler.hasCallBack(request)) {
            final Integer messageId = request.getUpdate().getCallbackQuery().getMessage().getMessageId();
            EditMessageText replyMarkup = EditMessageText.builder()
              .chatId(String.valueOf(userId))
              .text(text)
              .messageId(messageId + 1)
              .build();

            execute(replyMarkup);
        }
    }
    public void sendTyping(Long userId){
        SendChatAction chatAction = SendChatAction.builder()
          .action(ActionType.TYPING.toString())
          .chatId(String.valueOf(userId))
          .build();
        execute(chatAction);
    }

    public void sendMessage(Long userId, String text, ReplyKeyboard replyKeyboard) {
        sendTyping(userId);
        SendMessage sendMessage = SendMessage
          .builder()
          .text(text)
          .chatId(userId.toString())
          //Other possible parse modes: MARKDOWNV2, MARKDOWN, which allows to make text bold, and all other things
          .parseMode(ParseMode.HTML)
          .replyMarkup(replyKeyboard)
          .build();
        execute(sendMessage);
    }

    public void sendSticker(Long userId, String text) {
        sendTyping(userId);
        sendSticker(userId, text, null);
    }

    public void sendDocument(ByteArrayInputStream inputStream, Request request) {
        sendUploadDocument(request.getUserId());
        SendDocument sendDocument = SendDocument.builder()
          .chatId(request.getUserId().toString())
          .document(new InputFile(inputStream,"file.pdf"))// create filename
          .build();
        executeDocument(sendDocument);
    }

    private void sendUploadDocument(Long userId) {
        SendChatAction chatAction = SendChatAction.builder()
          .action(ActionType.UPLOADDOCUMENT.toString())
          .chatId(String.valueOf(userId))
          .build();
        execute(chatAction);
    }

    public void sendSticker(Long userId, String text, ReplyKeyboard replyKeyboard) {
        sendTyping(userId);
        SendSticker sendMessage = SendSticker.builder()
          .chatId(userId.toString())
          .sticker(new InputFile(text))
          .build();
        executeSticker(sendMessage);
    }

    private void execute(BotApiMethod botApiMethod) {
        try {
            botSender.execute(botApiMethod);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    private void executeSticker(SendSticker botApiMethod) {
        try {
            botSender.execute(botApiMethod);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    private void executeDocument(SendDocument sendDocument) {
        try {
            botSender.execute(sendDocument);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }


}
