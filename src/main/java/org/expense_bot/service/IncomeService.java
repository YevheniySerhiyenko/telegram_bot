package org.expense_bot.service;

import org.expense_bot.model.Income;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface IncomeService {

  Income save(Long userId, BigDecimal sum, LocalDateTime incomeDate);

  List<Income> getAllCurrentMonth(Long userId);

  List<Income> getAll(Long chatId, LocalDate date);

}
