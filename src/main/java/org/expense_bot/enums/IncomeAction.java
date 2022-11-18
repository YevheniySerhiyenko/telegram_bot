package org.expense_bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.handler.incomes.action_state.IncomeActionState;
import org.expense_bot.handler.incomes.action_state.IncomeBalanceHandler;
import org.expense_bot.handler.incomes.action_state.IncomeCheckHandler;
import org.expense_bot.handler.incomes.action_state.IncomeWriteHandler;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum IncomeAction {
  CHECK(Buttons.CHECK_INCOMES.getValue(), IncomeCheckHandler.class),
  WRITE(Buttons.WRITE_INCOMES.getValue(), IncomeWriteHandler.class),
  CHECK_BALANCE(Buttons.CHECK_BALANCE.getValue(), IncomeBalanceHandler.class);

  private final String value;
  private final Class<? extends IncomeActionState> handler;

  public static IncomeAction parse(String text) {
    return Arrays.stream(values())
      .filter(action -> action.getValue().equals(text))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unable to parse income action"));

  }
}
