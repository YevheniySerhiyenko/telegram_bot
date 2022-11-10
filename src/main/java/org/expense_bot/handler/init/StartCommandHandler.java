package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
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
    private final KeyboardBuilder keyboardBuilder;
    private final UserSessionService sessionService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {
        final Long chatId = request.getChatId();
        telegramService.sendMessage(chatId, Messages.HELLO_MESSAGE, keyboardBuilder.buildMainMenu());
        telegramService.sendMessage(chatId, Messages.CHOOSE_YOUR_ACTION);
        sessionService.updateState(chatId,ConversationState.Init.WAITING_INIT_ACTION);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
