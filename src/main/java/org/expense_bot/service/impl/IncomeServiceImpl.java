package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Income;
import org.expense_bot.repository.IncomeRepository;
import org.expense_bot.service.IncomeService;
import org.expense_bot.util.DateUtil;
import org.expense_bot.util.IncomeUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

  private final IncomeRepository incomeRepository;

  @Override
  public Income save(Long userId, BigDecimal sum, LocalDateTime incomeDate) {
	return incomeRepository.save(IncomeUtil.buildIncome(userId, sum, incomeDate));
  }

  @Override
  public List<Income> getAllCurrentMonth(Long userId) {
	return incomeRepository.getAllByUserIdAndIncomeDateIsAfter(userId, DateUtil.getBeginOfMonth());
  }


  @Override
  public List<Income> getAll(Long chatId, LocalDate date) {
	return incomeRepository.getAllBy(chatId, date.getMonthValue(), date.getYear());
  }


}
