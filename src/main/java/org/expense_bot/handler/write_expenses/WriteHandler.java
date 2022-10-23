package org.expense_bot.handler.write_expenses;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.TelegramService;
import org.expense_bot.service.UserSessionService;

@Component
public class WriteHandler extends UserRequestHandler {
    public static String writeDownSpendingMoney = "Записати витрати";
    public static List<String> categories = List.of("Книги", "Побутові потреби", "Шкідливі звички", "Гігієна та здоров'я", "Кафе", "Квартплата", "Кредит/борги", "Одяг та косметика", "Поїздки (транспорт, таксі)", "Продукти харчування", "Розваги та подарунки", "Зв'язок (телефон, інтернет)");
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public WriteHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    public boolean isApplicable(UserRequest userRequest) {
        return this.isTextMessage(userRequest.getUpdate(), writeDownSpendingMoney);
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
