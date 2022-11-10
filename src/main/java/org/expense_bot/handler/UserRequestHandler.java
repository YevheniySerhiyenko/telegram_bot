package org.expense_bot.handler;

import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.UserRequest;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class UserRequestHandler {

    public abstract boolean isApplicable(UserRequest request);
    public abstract void handle(UserRequest dispatchRequest);
    public abstract boolean isGlobal();

    public static boolean hasCallBack(UserRequest request) {
        return request.getUpdate().hasCallbackQuery();
    }

    public boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    public boolean isTextMessage(Update update, String text) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(text);
    }

    public static boolean hasMessage(UserRequest request) {
        return request.getUpdate().hasMessage();
    }

    public static String getUpdateData(UserRequest request) {
        String data = null;
        if(request.getUpdate().hasMessage()) {
            data = request.getUpdate().getMessage().getText();
        }
        if(request.getUpdate().hasCallbackQuery()) {
            data = request.getUpdate().getCallbackQuery().getData();
        }
        return data;
    }

    public static boolean isEqual(UserRequest request, ConversationState state) {
        return request.getUserSession().getState().equals(state);
    }

    public boolean isCommand(Update update, String command) {
        return update.hasMessage() && update.getMessage().isCommand()
          && update.getMessage().getText().equals(command);
    }

}