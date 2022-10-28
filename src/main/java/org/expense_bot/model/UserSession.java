package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSession {
    private Long chatId;
    private ConversationState state;
    private CategoryAction categoryAction;
    private String action;
    private String category;
    private Double sum;
    private String period;

    public UserSession(Long chatId, ConversationState state, String action, String category, Double sum, String period) {
        this.chatId = chatId;
        this.state = state;
        this.action = action;
        this.category = category;
        this.sum = sum;
        this.period = period;
    }

}
