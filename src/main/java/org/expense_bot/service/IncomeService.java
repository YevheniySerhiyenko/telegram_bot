package org.expense_bot.service;

import org.expense_bot.model.Income;

import java.time.LocalDateTime;
import java.util.List;

public interface IncomeService {

  Income save(Long userId, Double sum, LocalDateTime incomeDate);

  List<Income> getAllCurrentMonth(Long userId);

}
