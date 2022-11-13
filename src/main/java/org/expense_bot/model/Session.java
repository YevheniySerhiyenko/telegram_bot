package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.enums.StickerAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Session {
    private Long userId;
    private ConversationState state;
    private CategoryAction categoryAction;
    private String action;
    private String category;
    private BigDecimal expenseSum;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private BigDecimal incomeSum;
    private LocalDate expenseDate;
    private LocalDate incomeDate;
    private IncomeAction incomeAction;
    private String period;
    private StickerAction stickerAction;
    private List<Expense> expenseList;
    private List<Income> incomeList;
}
