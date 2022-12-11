package expense_bot.service;

import expense_bot.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

  Income save(Income income);

  List<Income> getAllCurrentMonth(Long userId);

  List<Income> getAll(Long userId, LocalDate date);

}
