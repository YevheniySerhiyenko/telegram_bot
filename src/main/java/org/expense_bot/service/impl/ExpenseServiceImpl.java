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

  public List<Expense> getByOneDay(Long chatId, String category) {
	final LocalDateTime from = DateUtil.getTodayMidnight();
	final LocalDateTime dateTo = DateUtil.getTomorrowMidnight();
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByChatIdAndDateTimeBetween(chatId, from, dateTo);
	}
	return expenseRepository.getAllByChatIdAndDateTimeBetweenAndCategory(chatId, from, dateTo, category);
  }


  public List<Expense> getByOneWeek(Long chatId, String category) {
	final LocalDateTime dateFrom = DateUtil.getStartOfWeek();
	final LocalDateTime dateTo = DateUtil.getTomorrowMidnight();
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByChatIdAndDateTimeBetween(chatId, dateFrom, dateTo);
	}
	return expenseRepository.getAllByChatIdAndDateTimeBetweenAndCategory(chatId, dateFrom, dateTo, category);
  }


  public List<Expense> getByOneMonth(Long chatId, String category) {
	final LocalDateTime dateFrom = DateUtil.getStartOfMonth();
	final LocalDateTime dateTo = DateUtil.getTomorrowMidnight();
	if(category.equals(Messages.BY_ALL_CATEGORIES)) {
	  return expenseRepository.getAllByChatIdAndDateTimeBetween(chatId, dateFrom, dateTo);
	}
	return expenseRepository.getAllByChatIdAndDateTimeBetweenAndCategory(chatId, dateFrom, dateTo, category);
  }

  public List<Expense> getByPeriod(Long chatId, LocalDate from, LocalDate to) {
//	return expenseRepository.getAllByDateTimeBetweenAndCategory(LocalDateTime.from(from), LocalDateTime.from(to), "");
	return null;
  }

  @Override
  public Expense update(Long chatId, Expense expense) {
	return expenseRepository.save(expense);
  }

}
