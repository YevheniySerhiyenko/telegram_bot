package org.expense_bot.handler.write_expenses;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class WriteHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private final UserService userService;

    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), Messages.WRITE_EXPENSES);
    }

    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = this.keyboardHelper.buildCategoriesMenu(userRequest.getChatId());
        this.telegramService.sendMessage(userRequest.getChatId(), Messages.CHOOSE_EXPENSES_CATEGORY, replyKeyboardMarkup);
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_CATEGORY);
        this.userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    public boolean isGlobal() {
        return true;
    }
}
