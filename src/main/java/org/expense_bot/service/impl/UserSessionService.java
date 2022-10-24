package org.expense_bot.service.impl;

import org.springframework.stereotype.Component;
import org.expense_bot.model.UserSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserSessionService {

    private final Map<Long, UserSession> userSessionMap = new HashMap<>();

    public UserSession getSession(Long chatId) {
        return userSessionMap.getOrDefault(chatId, UserSession
                .builder()
                .chatId(chatId)
                .build());
    }

    public UserSession saveSession(Long chatId, UserSession session) {
        return userSessionMap.put(chatId, session);
    }
    public UserSession getLastSession(Long chatId) {
        return userSessionMap.get(chatId);
    }

}
