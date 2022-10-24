package org.expense_bot.service.impl;

import org.expense_bot.model.Expense;
import org.expense_bot.repository.ExpenseRepository;
import org.expense_bot.service.ExpenseService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ExpenseServiceImpl implements ExpenseService {

  private static final int DAYS_TO_SUBTRACT = 7;
  private static final LocalDate NOW = LocalDate.now();
  private static final int FIRST_DAY = 1;
  private final ExpenseRepository expenseRepository;

  public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
	this.expenseRepository = expenseRepository;
  }

  public Expense save(Expense spent) {
	return expenseRepository.save(spent);
  }

  public List<Expense> getByOneDay() {
	final LocalDateTime from = LocalDateTime.from(NOW.atTime(LocalTime.MIDNIGHT));
	return expenseRepository.getAllByDateTimeIsAfter(from);
  }

  public List<Expense> getByOneWeek() {
	final LocalDateTime dateTime = NOW.minusDays(DAYS_TO_SUBTRACT).atTime(LocalTime.MIDNIGHT);
	return expenseRepository.getAllByDateTimeIsAfter(LocalDateTime.from(dateTime));
  }

  public List<Expense> getByOneMonth() {
	final LocalDateTime from = LocalDate.of(NOW.getYear(), NOW.getMonth(), FIRST_DAY).atTime(LocalTime.MIDNIGHT);
	return expenseRepository.getAllByDateTimeIsAfter(from);
  }

  public List<Expense> getByPeriod(LocalDate from, LocalDate to) {
	return expenseRepository.getAllByDateTimeBetween(LocalDateTime.from(from), LocalDateTime.from(to));
  }

}
