package org.expense_bot.service;

import org.expense_bot.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

  Expense save(Expense spent);

  List<Expense> getByOneDay();

  List<Expense> getByOneWeek();

  List<Expense> getByOneMonth();

  List<Expense> getByPeriod(LocalDate from, LocalDate to);

}
