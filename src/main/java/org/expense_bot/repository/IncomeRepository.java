package org.expense_bot.repository;

import org.expense_bot.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

  List<Income> getAllByUserIdAndIncomeDateIsAfter(Long userId, LocalDateTime beginOfMonth);

}
