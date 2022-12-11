package expense_bot.util;

import expense_bot.enums.CategoryAction;
import expense_bot.enums.ConversationState;
import expense_bot.enums.IncomeAction;
import expense_bot.enums.InitAction;
import expense_bot.enums.PasswordAction;
import expense_bot.model.Expense;
import expense_bot.model.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtil {

  public static Session getSession(Long userId, CategoryAction categoryAction) {
    return Session.builder()
      .userId(userId)
      .categoryAction(categoryAction)
      .state(ConversationState.Categories.WAITING_FINAL_ACTION)
      .build();
  }

  public static Session getSession(Long userId, String period, ConversationState state) {
    return Session.builder()
      .userId(userId)
      .period(period)
      .state(state)
      .build();
  }

  public static Session getSession(BigDecimal sum, Long userId) {
    return Session.builder()
      .userId(userId)
      .expenseSum(sum)
      .state(ConversationState.Init.WAITING_EXPENSE_ACTION)
      .build();
  }

  public static Session getSession(Long userId, List<Expense> expenses, String category) {
    return Session.builder()
      .userId(userId)
      .expenseList(expenses)
      .category(category)
      .state(ConversationState.Expenses.WAITING_ADDITIONAL_ACTION)
      .build();
  }

  public static Session getSession(Long userId, LocalDate date) {
    return Session.builder()
      .userId(userId)
      .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
      .expenseDate(date)
      .build();
  }

  public static Session buildSession(Long userId, String category) {
    return Session.builder()
      .userId(userId)
      .category(category)
      .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
      .build();
  }

  public static Session getSession(IncomeAction action, Long userId) {
    return Session.builder()
      .userId(userId)
      .incomeAction(action)
      .state(ConversationState.Incomes.WAITING_FOR_INCOME_SUM)
      .build();
  }

  public static Session getSession(Long userId, IncomeAction action) {
    return Session.builder()
      .userId(userId)
      .incomeAction(action)
      .build();
  }

  public static Session buildIncomeSession(Long userId, LocalDate localDate) {
    return Session.builder()
      .userId(userId)
      .incomeDate(localDate)
      .state(ConversationState.Incomes.WAITING_FOR_INCOME_SUM)
      .build();
  }

  public static Session getSession(Long userId) {
    return Session.builder()
      .userId(userId)
      .periodFrom(null)
      .periodTo(null)
      .state(ConversationState.Expenses.WAITING_ADDITIONAL_ACTION)
      .build();
  }

  public static Session getSession(Long userId, InitAction action) {
    return Session.builder()
      .userId(userId)
      .action(action.getValue())
      .state(ConversationState.Init.PASSWORD_ENTER)
      .build();
  }

  public static Session getSession(Long userId, PasswordAction passwordAction, String password) {
    return Session.builder()
      .userId(userId)
      .state(ConversationState.Settings.WAITING_FINAL_PASSWORD_ACTION)
      .passwordAction(passwordAction)
      .password(password)
      .build();
  }

  public static Session getSession(Long userId, boolean enablePassword) {
    return Session.builder()
      .userId(userId)
      .state(ConversationState.Settings.WAITING_INIT_PASSWORD_ACTION)
      .enablePassword(enablePassword)
      .build();
  }

  public static Session getSession(Long userId, PasswordAction passwordAction) {
    return Session.builder().userId(userId)
      .state(ConversationState.Settings.WAITING_PASSWORD_ACTION)
      .passwordAction(passwordAction)
      .build();
  }

  public static Session getSession(Long userId, String password) {
    return Session.builder()
      .userId(userId)
      .passwordConfirmed(password)
      .state(ConversationState.Settings.WAITING_INIT_PASSWORD_ACTION)
      .build();
  }

  public static Session getSession(String password, Long userId) {
    return Session.builder()
      .userId(userId)
      .password(password)
      .build();
  }

}
