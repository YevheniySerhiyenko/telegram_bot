package org.expense_bot.repository;

import org.expense_bot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  List<Expense> getAllByDateTimeEquals(LocalDate today);

  List<Expense> getAllByDateTimeIsAfter(LocalDate weekAgo);

  List<Expense> getAllByDateTimeBetween(LocalDate beginOfMonth, LocalDate today);

}
