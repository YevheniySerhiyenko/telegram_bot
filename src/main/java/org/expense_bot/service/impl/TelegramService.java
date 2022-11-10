package org.expense_bot.service.impl;

import com.itextpdf.text.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.model.UserRequest;
import org.expense_bot.sender.ExpenseBotSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramService {

    private final ExpenseBotSender botSender;

    public void editKeyboardMarkup(UserRequest request, InlineKeyboardMarkup replyKeyboard){
        final Long chatId = request.getChatId();
        if(UserRequestHandler.hasCallBack(request)) {
            final Integer messageId = request.getUpdate().getCallbackQuery().getMessage().getMessageId();

            execute(getReplyMarkup(replyKeyboard, chatId, messageId));
        }
    }

    private EditMessageReplyMarkup getReplyMarkup(InlineKeyboardMarkup replyKeyboard, Long chatId, Integer messageId) {
        return EditMessageReplyMarkup.builder()
          .chatId(String.valueOf(chatId))
          .replyMarkup(replyKeyboard)
          .messageId(messageId)
          .build();
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void editNextMessage(UserRequest request, String text) {
        final Long chatId = request.getChatId();
        if(UserRequestHandler.hasCallBack(request)){
            final Integer messageId = request.getUpdate().getCallbackQuery().getMessage().getMessageId();
            EditMessageText replyMarkup = EditMessageText.builder()
              .chatId(String.valueOf(chatId))
              .text(text)
              .messageId(messageId + 1)
              .build();

            execute(replyMarkup);
        }
    }

    public void sendAnswer(AnswerCallbackQuery query){
        execute(query);
    }
    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage
          .builder()
          .text(text)
          .chatId(chatId.toString())
          //Other possible parse modes: MARKDOWNV2, MARKDOWN, which allows to make text bold, and all other things
          .parseMode(ParseMode.HTML)
          .replyMarkup(replyKeyboard)
          .build();
        execute(sendMessage);
    }

    public void sendSticker(Long chatId, String text) {
        sendSticker(chatId, text, null);
    }

    public void sendDocument(ByteArrayInputStream path, UserRequest request) {
        SendDocument sendDocument = SendDocument.builder()
          .chatId(request.getChatId().toString())
          .document(new InputFile(path,"file.pdf"))// create filename
          .build();
        executeDocument(sendDocument);
    }

    public void sendSticker(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendSticker sendMessage = SendSticker.builder()
          .chatId(chatId.toString())
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
