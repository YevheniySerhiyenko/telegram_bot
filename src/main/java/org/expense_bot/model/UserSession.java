package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;

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
    private Double expenseSum;
    private Double incomeSum;
    private IncomeAction incomeAction;
    private String period;
}
