package org.expense_bot.service;

import org.expense_bot.model.Expense;
import org.expense_bot.repository.ExpenseRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExpenseServiceImpl implements ExpenseService {

  private static final int DAYS_TO_SUBTRACT = 7;
  private static final LocalDate NOW = LocalDate.now();
  private final ExpenseRepository expenseRepository;

  public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
	this.expenseRepository = expenseRepository;
  }

  public Expense save(Expense spent) {
	return expenseRepository.save(spent);
  }

  public List<Expense> getByOneDay() {
	return expenseRepository.getAllByDateTimeEquals(NOW);
  }

  public List<Expense> getByOneWeek() {
	LocalDate weekAgo = NOW.minusDays(7L);
	return expenseRepository.getAllByDateTimeIsAfter(weekAgo);
  }

  public List<Expense> getByOneMonth() {
	int dayOfMonth = NOW.getDayOfMonth();
	LocalDate beginOfMonth = LocalDate.of(NOW.getYear(), NOW.getMonth(), dayOfMonth);
	return expenseRepository.getAllByDateTimeBetween(beginOfMonth, NOW);
  }

  public List<Expense> getByPeriod(LocalDate from, LocalDate to) {
	return expenseRepository.getAllByDateTimeBetween(from, to);
  }

}
