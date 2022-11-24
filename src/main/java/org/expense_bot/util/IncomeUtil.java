package org.expense_bot.util;

import org.expense_bot.model.Income;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class IncomeUtil {

  private static final LocalDateTime NOW = LocalDateTime.now();

  public static String getIncome(Income income) {
	return DateUtil.getDate(income.getIncomeDate()) + " - " + income.getSum() + " грн";
  }

  public static Income buildIncome(Long userId, BigDecimal sum, LocalDate incomeDate) {
	return Income.builder()
	  .userId(userId)
	  .sum(sum)
	  .incomeDate(incomeDate == null ? NOW : incomeDate.atStartOfDay())
	  .build();
  }

  public static BigDecimal getSum(List<Income> incomes) {
	return incomes.stream()
	  .map(Income::getSum)
	  .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
