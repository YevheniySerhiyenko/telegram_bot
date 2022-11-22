package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.Button;
import org.expense_bot.model.Expense;
import org.expense_bot.repository.ExpenseRepository;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;

  @Override
  public Expense save(Expense expense) {
	return expenseRepository.save(expense);
  }

  @Override
  public List<Expense> getByPeriod(Long userId, LocalDateTime from, LocalDateTime to, String category) {
	final LocalDateTime tomorrowMidnight = DateUtil.getTomorrowMidnight(to.toLocalDate());
	if(category.equals(Button.BY_ALL_CATEGORIES.getValue())) {
	  return expenseRepository.getAllByUserIdAndDateTimeBetween(userId, from, tomorrowMidnight);
	}
	return expenseRepository.getAllByUserIdAndDateTimeBetweenAndCategory(userId, from, tomorrowMidnight, category);
  }

  @Override
  public Expense update(Long userId, Expense expense) {
	return expenseRepository.save(expense);
  }

}
