package org.expense_bot.handler;

import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.Request;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class RequestHandler {

    public abstract boolean isApplicable(Request request);
    public abstract void handle(Request dispatchRequest);
    public abstract boolean isGlobal();

    public static boolean hasCallBack(Request request) {
        return request.getUpdate().hasCallbackQuery();
    }

    public boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    public boolean isTextMessage(Update update, String text) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(text);
    }

    public static boolean hasMessage(Request request) {
        return request.getUpdate().hasMessage();
    }

    public static String getUpdateData(Request request) {
        if(request.getUpdate().hasMessage()) {
          return request.getUpdate().getMessage().getText();
        }
        return request.getUpdate().getCallbackQuery().getData();
    }

    public static boolean isStateEqual(Request request, ConversationState state) {
        return request.getSession().getState().equals(state);
    }

    public boolean isCommand(Update update, String command) {
        return update.hasMessage() && update.getMessage().isCommand()
          && update.getMessage().getText().equals(command);
    }

}