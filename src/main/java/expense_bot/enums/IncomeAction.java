package expense_bot.enums;

import expense_bot.constant.ErrorMessages;
import expense_bot.handler.incomes.action_state.IncomeActionState;
import expense_bot.handler.incomes.action_state.IncomeBalanceHandler;
import expense_bot.handler.incomes.action_state.IncomeCheckHandler;
import expense_bot.handler.incomes.action_state.IncomeWriteHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum IncomeAction {
  CHECK(Button.CHECK_INCOMES.getValue(), IncomeCheckHandler.class),
  WRITE(Button.WRITE_INCOMES.getValue(), IncomeWriteHandler.class),
  CHECK_BALANCE(Button.CHECK_BALANCE.getValue(), IncomeBalanceHandler.class);

  private final String value;
  private final Class<? extends IncomeActionState> handler;

  public static IncomeAction parse(String text) {
    return Arrays.stream(values())
      .filter(action -> action.getValue().equals(text))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PARSE_INCOME_ACTION_ERROR));
  }
}
