package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Income;
import org.expense_bot.repository.IncomeRepository;
import org.expense_bot.service.IncomeService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

  private final IncomeRepository incomeRepository;

  private static final LocalDateTime NOW = LocalDateTime.now();
  private static final int FIRST_DAY = 1;


  @Override
  public Income save(Long userId, BigDecimal sum, LocalDateTime incomeDate) {
	return incomeRepository.save(buildIncome(userId, sum, incomeDate));
  }

  @Override
  public List<Income> getAllCurrentMonth(Long userId) {
	final LocalDateTime dateTime = LocalDate.of(NOW.getYear(), NOW.getMonth(), FIRST_DAY).atTime(LocalTime.MIDNIGHT);
	return incomeRepository.getAllByUserIdAndIncomeDateIsAfter(userId, dateTime);
  }

  @Override
  public List<Income> getAll(Long chatId, Month month) {

    return null;
  }

  private Income buildIncome(Long userId, BigDecimal sum, LocalDateTime incomeDate) {
	return Income.builder()
	  .userId(userId)
	  .sum(sum)
	  .incomeDate(incomeDate)
	  .build();
  }

}
