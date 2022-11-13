package org.expense_bot.service;

import org.expense_bot.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

  Income save(Income income);

  List<Income> getAllCurrentMonth(Long userId);

  List<Income> getAll(Long chatId, LocalDate date);

}
