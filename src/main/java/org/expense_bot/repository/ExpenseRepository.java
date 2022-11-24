package org.expense_bot.repository;

import org.expense_bot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


  List<Expense> getAllByUserIdAndDateTimeBetween(Long userId, LocalDateTime dateFrom, LocalDateTime dateTo);

  List<Expense> getAllByUserIdAndDateTimeBetweenAndCategory(Long userId, LocalDateTime dateFrom, LocalDateTime dateTo, String category);


}
