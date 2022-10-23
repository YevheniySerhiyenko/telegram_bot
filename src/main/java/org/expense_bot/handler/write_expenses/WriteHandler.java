package org.expense_bot.handler.write_expenses;

import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.TelegramService;
import org.expense_bot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class WriteHandler extends UserRequestHandler {
    public static String writeDownSpendingMoney = "Записати витрати";
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public WriteHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), writeDownSpendingMoney);
    }

    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = this.keyboardHelper.buildCategoriesMenu();
        this.telegramService.sendMessage(userRequest.getChatId(), "Оберіть категорію витрат зі списку або опишіть вручну⤵️", replyKeyboardMarkup);
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_CATEGORY);
        this.userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    public boolean isGlobal() {
        return true;
    }
}
