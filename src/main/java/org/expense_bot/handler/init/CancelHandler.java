package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static org.expense_bot.constant.Constants.BUTTON_CANCEL;

@Component
@RequiredArgsConstructor
public class CancelHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest request) {
        return isTextMessage(request.getUpdate(), BUTTON_CANCEL);
    }

    @Override
    public void handle(UserRequest request) {
        final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildExpenseMenu();
        telegramService.sendMessage(request.getChatId(), "Обирайте з меню нижче ⤵️", keyboard);
        userSessionService.updateState(request.getChatId(), ConversationState.Init.CONVERSATION_STARTED);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
