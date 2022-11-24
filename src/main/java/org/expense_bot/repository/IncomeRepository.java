package org.expense_bot.repository;

import org.expense_bot.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

  List<Income> getAllByUserIdAndIncomeDateIsAfter(Long userId, LocalDateTime beginOfMonth);

  @Query(nativeQuery = true,
	value = ""
	  + " select i.* "
	  + "   from incomes i "
	  + "  where user_id = ?1"
	  + "    and extract(MONTH from income_date) = ?2"
	  + "    and extract(YEAR from income_date) = ?3")
  List<Income> getAllBy(Long userId, Integer month, Integer year);

}
