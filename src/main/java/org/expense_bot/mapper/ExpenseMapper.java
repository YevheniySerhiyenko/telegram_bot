package org.expense_bot.mapper;

import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.model.Expense;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseMapper {

  public static List<ExpenseGroup> toGroup(List<Expense> expenses){

	return expenses.stream()
	  .collect(Collectors.groupingBy(Expense::getCategory))
	  .keySet()
	  .stream()
	  .map(category ->
		getExpenseGroup(expenses.stream()
		  .collect(Collectors.groupingBy(Expense::getCategory))
		  .get(category), category))
	  .collect(Collectors.toList());
  }

  private static ExpenseGroup getExpenseGroup( List<Expense> expenses, String category) {
	return new ExpenseGroup(category, expenses.stream()
	  .map(Expense::getSum)
	  .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

}
