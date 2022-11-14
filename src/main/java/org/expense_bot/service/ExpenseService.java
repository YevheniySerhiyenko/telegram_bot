package org.expense_bot.service;

import org.expense_bot.model.Expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseService {

  Expense save(Expense spent);

  List<Expense> getByPeriod(Long chatId, LocalDateTime from, LocalDateTime to, String category);

  Expense update(Long chatId, Expense expense);

}
