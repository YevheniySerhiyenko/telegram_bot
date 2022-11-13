package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.constant.Commands;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends RequestHandler {

    private final TelegramService telegramService;
    private final KeyboardBuilder keyboardBuilder;
    private final SessionService sessionService;

    @Override
    public boolean isApplicable(Request userRequest) {
        return isCommand(userRequest.getUpdate(), Commands.START);
    }

    @Override
    public void handle(Request request) {
        final Long chatId = request.getUserId();
        telegramService.sendMessage(chatId, Messages.HELLO_MESSAGE, keyboardBuilder.buildMainMenu());
        telegramService.sendMessage(chatId, Messages.CHOOSE_YOUR_ACTION);
        sessionService.updateState(chatId,ConversationState.Init.WAITING_INIT_ACTION);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
