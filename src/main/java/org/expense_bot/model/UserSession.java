package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private BigDecimal expenseSum;
    private BigDecimal incomeSum;
    private LocalDate expenseDate;
    private LocalDate incomeDate;
    private IncomeAction incomeAction;
    private String period;
    private String stickerAction;
}
