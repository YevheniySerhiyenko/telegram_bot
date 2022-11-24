package org.expense_bot.mapper;

import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.model.Expense;
import org.expense_bot.util.DateUtil;
import org.expense_bot.util.ExpenseUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseMapper {

  public static List<ExpenseGroup> toGroup(List<Expense> expenses) {

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

  private static ExpenseGroup getExpenseGroup(List<Expense> expenses, String category) {
	return ExpenseGroup.builder()
	  .category(category)
	  .sum(ExpenseUtil.getSum(expenses))
	  .build();
  }

  public static List<String> toDetailExpense(List<Expense> expenses) {
	return expenses.stream()
	  .sorted(Comparator.comparing(Expense::getDateTime))
	  .map(expense ->
		"\n\uD83D\uDD34\t\t" + expense.getSum()
		  + " грн  \t\t\uD83D\uDCC5\t"
		  + DateUtil.getDateTime(expense.getDateTime())
		  + " 🕢"
		  + DateUtil.getTime(expense.getDateTime()))
	  .collect(Collectors.toList());
  }

}
