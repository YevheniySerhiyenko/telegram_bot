package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.expenses.write.SumEnteredHandler;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSessionService {

    private final Map<Long, UserSession> userSessionMap = new HashMap<>();
    private final TelegramService telegramService;

    public UserSession getSession(Long chatId) {
        return userSessionMap.getOrDefault(chatId, UserSession
          .builder()
          .chatId(chatId)
          .build());
    }

    private void saveSession(UserSession session) {
        userSessionMap.put(session.getChatId(), session);
    }

    public void updateState(Long chatId, ConversationState state) {
        final UserSession session = getSession(chatId);
        session.setState(state);
        saveSession(session);
    }

    public void update(UserSession userSession) {
        final UserSession session = getSession(userSession.getChatId());
        Optional.ofNullable(userSession.getState()).ifPresent(session::setState);
        Optional.ofNullable(userSession.getCategoryAction()).ifPresent(session::setCategoryAction);
        Optional.ofNullable(userSession.getAction()).ifPresent(session::setAction);
        Optional.ofNullable(userSession.getCategory()).ifPresent(session::setCategory);
        Optional.ofNullable(userSession.getExpenseSum()).ifPresent(session::setExpenseSum);
        Optional.ofNullable(userSession.getPeriodFrom()).ifPresent(session::setPeriodFrom);
        Optional.ofNullable(userSession.getPeriodTo()).ifPresent(session::setPeriodTo);
        Optional.ofNullable(userSession.getIncomeSum()).ifPresent(session::setIncomeSum);
        Optional.ofNullable(userSession.getExpenseDate()).ifPresent(session::setExpenseDate);
        Optional.ofNullable(userSession.getIncomeDate()).ifPresent(session::setIncomeDate);
        Optional.ofNullable(userSession.getIncomeAction()).ifPresent(session::setIncomeAction);
        Optional.ofNullable(userSession.getPeriod()).ifPresent(session::setPeriod);
        Optional.ofNullable(userSession.getStickerAction()).ifPresent(session::setStickerAction);
        Optional.ofNullable(userSession.getExpenseList()).ifPresent(session::setExpenseList);
        Optional.ofNullable(userSession.getIncomeList()).ifPresent(session::setIncomeList);
        saveSession(session);
    }

    public void checkEnteredDate(UserRequest request, ConversationState state, Object clazz) {
        if(UserRequestHandler.hasMessage(request)) {
            final String text = UserRequestHandler.getUpdateData(request);
            if(Objects.equals(text, Messages.ENTER_DATE)) {
                final ReplyKeyboard calendar = Calendar.buildCalendar(LocalDate.now());
                telegramService.sendMessage(request.getChatId(), Messages.ENTER_DATE, calendar);
                updateState(request.getChatId(), state);
                throw new RuntimeException("Build calendar");
            }
        }
        if(UserRequestHandler.hasCallBack(request)) {
            final Long chatId = request.getChatId();
            final InlineKeyboardMarkup keyboard = Calendar.changeMonth(request);
            if(Objects.isNull(keyboard)) {
                final LocalDate date = Calendar.getDate(request);
                telegramService.editNextMessage(request, String.format(Messages.DATE, date));
                updateSession(clazz, chatId, date);
                throw new RuntimeException("Change day");
            }
            telegramService.editKeyboardMarkup(request, keyboard);
        }
    }

    private void updateSession(Object clazz, Long chatId, LocalDate date) {
        if(clazz instanceof SumEnteredHandler){
            update(SessionUtil.buildExpenseSession(chatId, date));
        }else {
            update(SessionUtil.buildIncomeSession(chatId, date));
        }
    }


}
