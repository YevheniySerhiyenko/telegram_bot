package org.expense_bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.expense_bot.handler.expenses.action_state.CheckExpenseHandler;
import org.expense_bot.handler.expenses.action_state.ExpenseActionState;
import org.expense_bot.handler.expenses.action_state.WriteExpenseHandler;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ExpenseAction {
  CHECK(Button.CHECK_EXPENSES.getValue(), CheckExpenseHandler.class),
  WRITE(Button.WRITE_EXPENSES.getValue(), WriteExpenseHandler.class);


  private final String value;
  private final Class<? extends ExpenseActionState> handler;

  public static ExpenseAction parse(String text) {
	return Arrays.stream(values())
	  .filter(action -> action.getValue().equals(text))
	  .findFirst()
	  .orElseThrow(() -> new IllegalArgumentException("Unable to parse init action"));

  }
}
