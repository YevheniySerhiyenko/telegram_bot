package org.expense_bot.service;

import org.expense_bot.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

  Expense save(Expense spent);

  List<Expense> getByOneDay(Long chatId, String category);

  List<Expense> getByOneWeek(Long chatId, String category);

  List<Expense> getByOneMonth(Long chatId, String category);

  List<Expense> getByPeriod(Long chatId, LocalDate from, LocalDate to, String category);

  Expense update(Long chatId, Expense expense);

}
