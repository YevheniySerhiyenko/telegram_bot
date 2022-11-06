package org.expense_bot.repository;

import org.expense_bot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  List<Expense> getAllByDateTimeIsAfter(LocalDateTime dateTime);

  List<Expense> getAllByDateTimeIsAfterAndCategory(LocalDateTime dateTime,String category);

  List<Expense> getAllByChatIdAndDateTimeBetween(Long chatId,LocalDateTime dateFrom, LocalDateTime dateTo);

  @Query("select e from Expense e where e.chatId = ?1 and e.dateTime between ?2 and ?3 and e.category = ?4")
  List<Expense> getAllByChatIdAndDateTimeBetweenAndCategory(Long chatId, LocalDateTime dateFrom, LocalDateTime dateTo, String category);


}
