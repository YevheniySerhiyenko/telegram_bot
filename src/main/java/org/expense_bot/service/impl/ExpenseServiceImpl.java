package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.Expense;
import org.expense_bot.repository.ExpenseRepository;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;

  public Expense save(Expense expense) {
	return expenseRepository.save(expense);
  }

  @Override
  public List<Expense> getByOneDay(Long chatId, String category) {
	final LocalDateTime from = DateUtil.getTodayMidnight();
	final LocalDateTime to = DateUtil.getTomorrowMidnight();
	return getExpenses(chatId, category, from, to);
  }

  @Override
  public List<Expense> getByOneWeek(Long chatId, String category) {
	final LocalDateTime from = DateUtil.getStartOfWeek();
	final LocalDateTime to = DateUtil.getTomorrowMidnight();
	return getExpenses(chatId, category, from, to);
  }

  @Override
  public List<Expense> getByOneMonth(Long chatId, String category) {
	final LocalDateTime from = DateUtil.getStartOfMonth();
	final LocalDateTime to = DateUtil.getTomorrowMidnight();
	return getExpenses(chatId, category, from, to);
  }

  @Override
  public List<Expense> getByPeriod(Long chatId, LocalDateTime from, LocalDateTime to, String category) {
	return getExpenses(chatId, category, from, DateUtil.getTomorrowMidnight(to.toLocalDate()));
  }

  @Override
  public Expense update(Long chatId, Expense expense) {
	return expenseRepository.save(expense);
  }

  private List<Expense> getExpenses(Long chatId, String category, LocalDateTime from, LocalDateTime to) {
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByChatIdAndDateTimeBetween(chatId, from, to);
	}
	return expenseRepository.getAllByChatIdAndDateTimeBetweenAndCategory(chatId, from, to, category);
  }

}
