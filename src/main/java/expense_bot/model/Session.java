package expense_bot.model;

import expense_bot.enums.CategoryAction;
import expense_bot.enums.ConversationState;
import expense_bot.enums.IncomeAction;
import expense_bot.enums.PasswordAction;
import expense_bot.enums.StickerAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
  private String password;
  private String passwordConfirmed;
  private boolean enablePassword;
  private PasswordAction passwordAction;

}
