package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.Expense;
import org.expense_bot.repository.ExpenseRepository;
import org.expense_bot.service.ExpenseService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;

  private static final int DAYS_TO_SUBTRACT = 7;
  private static final LocalDate NOW = LocalDate.now();
  private static final int FIRST_DAY = 1;

  public Expense save(Expense expense) {
	return expenseRepository.save(expense);
  }

  public List<Expense> getByOneDay(Long chatId, String category) {
	final LocalDateTime from = LocalDateTime.from(NOW.atTime(LocalTime.MIDNIGHT));
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByDateTimeIsAfter(from);
	}
	return expenseRepository.getAllByDateTimeIsAfterAndCategory(from, category);
  }

  public List<Expense> getByOneWeek(Long chatId, String category) {
	final LocalDateTime dateTime = NOW.minusDays(DAYS_TO_SUBTRACT).atTime(LocalTime.MIDNIGHT);
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByDateTimeIsAfter(dateTime);
	}
	return expenseRepository.getAllByDateTimeIsAfterAndCategory(LocalDateTime.from(dateTime), category);
  }

  public List<Expense> getByOneMonth(Long chatId, String category) {
	final LocalDateTime dateTime = LocalDate.of(NOW.getYear(), NOW.getMonth(), FIRST_DAY).atTime(LocalTime.MIDNIGHT);
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByDateTimeIsAfter(dateTime);
	}
	return expenseRepository.getAllByDateTimeIsAfterAndCategory(dateTime, category);
  }

  public List<Expense> getByPeriod(LocalDate from, LocalDate to) {
	return expenseRepository.getAllByDateTimeBetweenAndCategory(LocalDateTime.from(from), LocalDateTime.from(to), "");
  }

}
