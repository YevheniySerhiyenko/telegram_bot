package org.expense_bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.handler.init.action_state.InitActionState;
import org.expense_bot.handler.init.action_state.InitExpenseHandler;
import org.expense_bot.handler.init.action_state.InitIncomesHandler;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum InitAction {
  EXPENSES(Messages.EXPENSES, InitExpenseHandler.class),
  INCOMES(Messages.INCOMES, InitIncomesHandler.class);

  private final String value;
  private final Class<? extends InitActionState> handler;

  public static InitAction parseAction(String text) {
	return Arrays.stream(values())
	  .filter(action -> action.getValue().equals(text))
	  .findFirst()
	  .orElseThrow(() -> new RuntimeException("Unable to parse init action"));

  }
}
