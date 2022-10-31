package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WriteHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private final UserCategoryService userCategoryService;
    private final BackButtonHandler backButtonHandler;

    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
          && ConversationState.WAITING_EXPENSE_ACTION.equals(userRequest.getUserSession().getState());

    }

    public void handle(UserRequest userRequest) {
        backButtonHandler.handleMainMenuBackButton(userRequest);
        final Long chatId = userRequest.getChatId();
        final List<UserCategory> byUserId = userCategoryService.getByUserId(chatId);
        checkEmpty(chatId, byUserId);
        final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCategoriesMenu(chatId);
        telegramService.sendMessage(chatId, Messages.CHOOSE_EXPENSES_CATEGORY, replyKeyboardMarkup);
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_CATEGORY);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    private void checkEmpty(Long chatId, List<UserCategory> byUserId) {
        if(byUserId.isEmpty()) {
            telegramService.sendMessage(chatId, Messages.NO_ONE_CATEGORY_FOUND, keyboardHelper.buildBackButtonMenu());
            throw new RuntimeException(Messages.NOTHING_TO_DELETE);
        }
    }

    public boolean isGlobal() {
        return true;
    }
}
