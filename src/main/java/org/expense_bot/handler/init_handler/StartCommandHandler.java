package org.expense_bot.handler.init_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.TelegramService;

@Component
@RequiredArgsConstructor
public class StartCommandHandler extends UserRequestHandler {

    private static final String command = "/start";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {
        final ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(request.getChatId(),
                "\uD83D\uDC4BПривіт! За допомогою цього чат-бота ви зможете зробити запит про допомогу!",
                replyKeyboard);
        telegramService.sendMessage(request.getChatId(),
                "Обирайте що хочете зробити ⤵️");
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
