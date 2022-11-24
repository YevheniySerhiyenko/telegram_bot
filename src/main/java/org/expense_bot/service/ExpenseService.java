package org.expense_bot.service;

import org.expense_bot.model.Expense;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseService {

  Expense save(Expense expense);

  List<Expense> getByPeriod(Long userId, LocalDateTime from, LocalDateTime to, String category);

  Expense update(Long userId, Expense expense);

}
