package org.expense_bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.handler.incomes.action_state.IncomeActionState;
import org.expense_bot.handler.incomes.action_state.IncomeBalanceHandler;
import org.expense_bot.handler.incomes.action_state.IncomeCheckHandler;
import org.expense_bot.handler.incomes.action_state.IncomeWriteHandler;
import org.expense_bot.handler.init.action_state.InitActionState;
import org.expense_bot.handler.init.action_state.InitExpenseHandler;
import org.expense_bot.handler.init.action_state.InitIncomesHandler;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum IncomeAction {
  CHECK(Messages.CHECK_INCOMES, IncomeCheckHandler.class),
  WRITE(Messages.WRITE_INCOMES, IncomeWriteHandler.class),
  CHECK_BALANCE(Messages.CHECK_BALANCE, IncomeBalanceHandler.class);

  private final String value;
  private final Class<? extends IncomeActionState> handler;

  public static IncomeAction parse(String text) {
    return Arrays.stream(values())
      .filter(action -> action.getValue().equals(text))
      .findFirst()
      .orElseThrow(() -> new RuntimeException("Unable to parse income action"));

  }
}
