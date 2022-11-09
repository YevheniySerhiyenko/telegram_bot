package org.expense_bot.service.impl;

import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.UserSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserSessionService {

    private final Map<Long, UserSession> userSessionMap = new HashMap<>();

    public UserSession getSession(Long chatId) {
        return userSessionMap.getOrDefault(chatId, UserSession
          .builder()
          .chatId(chatId)
          .build());
    }

    public UserSession saveSession(UserSession session) {
        return userSessionMap.put(session.getChatId(), session);
    }


    public void updateState(Long chatId, ConversationState state) {
        final UserSession session = getSession(chatId);
        session.setState(state);
        saveSession(session);
    }

    public void updateSession(UserSession userSession) {
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
        saveSession(session);
    }

}
