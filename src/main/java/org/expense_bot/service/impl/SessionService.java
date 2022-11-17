package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.expenses.write.SumEnteredHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
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
public class SessionService {

    private final Map<Long, Session> userSessionMap = new HashMap<>();
    private final TelegramService telegramService;

    public Session getSession(Long userId) {
        return userSessionMap.getOrDefault(userId, Session
          .builder()
          .userId(userId)
          .build());
    }

    private void saveSession(Session session) {
        userSessionMap.put(session.getUserId(), session);
    }

    public void updateState(Long userId, ConversationState state) {
        final Session session = getSession(userId);
        session.setState(state);
        saveSession(session);
    }

    public void update(Session userSession) {
        final Session session = getSession(userSession.getUserId());
        Optional.ofNullable(userSession.getState()).ifPresent(session::setState);
        Optional.ofNullable(userSession.getCategoryAction()).ifPresent(session::setCategoryAction);
        Optional.ofNullable(userSession.getAction()).ifPresent(session::setAction);
        Optional.ofNullable(userSession.getCategory()).ifPresent(session::setCategory);
        Optional.ofNullable(userSession.getExpenseSum()).ifPresent(session::setExpenseSum);
        Optional.ofNullable(userSession.getIncomeSum()).ifPresent(session::setIncomeSum);
        Optional.ofNullable(userSession.getExpenseDate()).ifPresent(session::setExpenseDate);
        Optional.ofNullable(userSession.getIncomeDate()).ifPresent(session::setIncomeDate);
        Optional.ofNullable(userSession.getIncomeAction()).ifPresent(session::setIncomeAction);
        Optional.ofNullable(userSession.getPeriod()).ifPresent(session::setPeriod);
        Optional.ofNullable(userSession.getStickerAction()).ifPresent(session::setStickerAction);
        Optional.ofNullable(userSession.getExpenseList()).ifPresent(session::setExpenseList);
        Optional.ofNullable(userSession.getIncomeList()).ifPresent(session::setIncomeList);
        session.setPeriodFrom(userSession.getPeriodFrom());
        session.setPeriodTo(userSession.getPeriodTo());
        saveSession(session);
    }

    public void checkEnteredDate(Request request, ConversationState state, Object clazz) {
        if(RequestHandler.hasMessage(request)) {
            final String text = RequestHandler.getUpdateData(request);
            if(Objects.equals(text, Messages.ENTER_DATE)) {
                final ReplyKeyboard calendar = Calendar.buildCalendar(LocalDate.now());
                telegramService.sendMessage(request.getUserId(), Messages.ENTER_DATE, calendar);
                updateState(request.getUserId(), state);
                return;
            }
        }
        if(RequestHandler.hasCallBack(request)) {
            final Long userId = request.getUserId();
            final InlineKeyboardMarkup keyboard = Calendar.changeMonth(request);
            if(Objects.isNull(keyboard)) {
                final LocalDate date = Calendar.getDate(request);
                telegramService.editNextMessage(request, String.format(Messages.DATE, date));
                updateSession(clazz, userId, date);
                return;
            }
            telegramService.editKeyboardMarkup(request, keyboard);
        }
    }

    private void updateSession(Object clazz, Long userId, LocalDate date) {
        if(clazz instanceof SumEnteredHandler) {
            update(SessionUtil.buildExpenseSession(userId, date));
        } else {
            update(SessionUtil.buildIncomeSession(userId, date));
        }
    }


}
