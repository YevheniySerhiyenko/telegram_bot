package org.expense_bot.util;

import org.expense_bot.model.Income;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IncomeUtil {

  public static String getIncome(Income income) {
	return getDate(income.getIncomeDate()) + " - " + income.getSum() + " грн";
  }

  private static String getDate(LocalDateTime localDate) {
	return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public static Income buildIncome(Long userId, BigDecimal sum, LocalDateTime incomeDate) {
	return Income.builder()
	  .userId(userId)
	  .sum(sum)
	  .incomeDate(incomeDate)
	  .build();
  }
}
