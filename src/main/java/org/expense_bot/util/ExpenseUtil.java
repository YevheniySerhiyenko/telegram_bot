package org.expense_bot.util;

import org.expense_bot.constant.Messages;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class ExpenseUtil {

  private static final LocalDateTime NOW = LocalDateTime.now();

  public static Expense getExpense(UserSession session) {
	return Expense.builder()
	  .category(session.getCategory())
	  .sum(session.getExpenseSum())
	  .chatId(session.getChatId())
	  .dateTime(session.getExpenseDate() != null ? session.getExpenseDate().atStartOfDay() : NOW)
	  .build();
  }

  public static String getMessage(Expense expense) {
	return expense.getCategory() + " : " + expense.getSum();
  }

  public static String getSumMessage(List<Expense> expenses, String period) {
	final BigDecimal sum = expenses.stream().map(Expense::getSum).reduce(BigDecimal::add).orElse(null);
	return String.format(Messages.RESPONSE_MESSAGE, period.toLowerCase(Locale.ROOT)) + sum;
  }

  public static BigDecimal getSum(List<Expense> expenses) {
	return expenses.stream()
	  .map(Expense::getSum)
	  .reduce(BigDecimal.ZERO, BigDecimal::add);
  }


}