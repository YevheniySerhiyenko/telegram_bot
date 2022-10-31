package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends UserRequestHandler {

    private static final String command = "/expenses";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService sessionService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {
        final ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(request.getChatId(), Messages.HELLO_MESSAGE, replyKeyboard);
        telegramService.sendMessage(request.getChatId(), Messages.CHOOSE_YOUR_ACTION);
        final UserSession session = request.getUserSession();
        session.setState(ConversationState.CONVERSATION_STARTED);
        sessionService.saveSession(request.getChatId(), session);
        log.info(request.getUserSession().toString());
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
