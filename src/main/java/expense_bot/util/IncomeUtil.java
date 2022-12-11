package expense_bot.util;

import expense_bot.model.Income;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IncomeUtil {

  public static String getIncome(Income income) {
    return DateUtil.getDate(income.getIncomeDate()) + " - " + income.getSum() + " грн";
  }

  public static Income buildIncome(Long userId, BigDecimal sum, LocalDate incomeDate) {
    return Income.builder()
      .userId(userId)
      .sum(sum)
      .incomeDate(incomeDate == null ? LocalDateTime.now() : incomeDate.atStartOfDay())
      .build();
  }

  public static BigDecimal getSum(List<Income> incomes) {
    return incomes.stream()
      .map(Income::getSum)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
