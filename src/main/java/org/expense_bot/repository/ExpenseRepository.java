package org.expense_bot.repository;

import org.expense_bot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  List<Expense> getAllByDateTimeIsAfter(LocalDateTime dateTime);

  List<Expense> getAllByDateTimeIsAfterAndCategory(LocalDateTime dateTime,String category);

  List<Expense> getAllByDateTimeBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

  List<Expense> getAllByDateTimeBetweenAndCategory(LocalDateTime dateFrom, LocalDateTime dateTo, String category);

}
