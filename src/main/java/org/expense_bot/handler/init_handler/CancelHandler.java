package org.expense_bot.handler.init_handler;

import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

import static org.expense_bot.constant.Constants.BTN_CANCEL;

@Component
public class CancelHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;

    public CancelHandler(TelegramService telegramService, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), BTN_CANCEL);
    }

    @Override
    public void handle(UserRequest userRequest) {
        telegramService.sendMessage(userRequest.getChatId(),"Обирайте з меню нижче ⤵️");

        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
