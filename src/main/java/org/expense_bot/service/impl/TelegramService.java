package org.expense_bot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.expense_bot.model.UserRequest;
import org.expense_bot.sender.ExpenseBotSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Slf4j
@Component
public class TelegramService {

    private final ExpenseBotSender botSender;

    public TelegramService(ExpenseBotSender botSender) {
        this.botSender = botSender;
    }

    public void editMessage(UserRequest userRequest, InlineKeyboardMarkup replyKeyboard){
        final Long chatId = userRequest.getChatId();
        if(userRequest.getUpdate().hasCallbackQuery()) {
            final Integer messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();
            EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
              .chatId(String.valueOf(chatId))
              .replyMarkup(replyKeyboard)
              .messageId(messageId)
              .build();

            execute(replyMarkup);
        }
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
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

}
