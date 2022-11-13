package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static org.expense_bot.constant.Buttons.BUTTON_CANCEL;

@Component
@RequiredArgsConstructor
public class CancelHandler extends RequestHandler {

    private final TelegramService telegramService;
    private final SessionService sessionService;
    private final KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(Request request) {
        return isTextMessage(request.getUpdate(), BUTTON_CANCEL);
    }

    @Override
    public void handle(Request request) {
        final ReplyKeyboard keyboard = keyboardBuilder.buildExpenseMenu();
        telegramService.sendMessage(request.getUserId(), "Обирайте з меню нижче ⤵️", keyboard);
        sessionService.updateState(request.getUserId(), ConversationState.Init.CONVERSATION_STARTED);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
