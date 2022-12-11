package expense_bot.enums;

import expense_bot.constant.ErrorMessages;
import expense_bot.handler.init.action_state.InitActionState;
import expense_bot.handler.init.action_state.InitExpenseHandler;
import expense_bot.handler.init.action_state.InitIncomesHandler;
import expense_bot.handler.init.action_state.PasswordValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum InitAction {
  EXPENSES(Button.EXPENSES.getValue(), InitExpenseHandler.class),
  INCOMES(Button.INCOMES.getValue(), InitIncomesHandler.class),
  PASSWORD(null, PasswordValidator.class);

  private final String value;
  private final Class<? extends InitActionState> handler;

  public static InitAction parseAction(String text) {
    return Arrays.stream(values())
      .filter(action -> action.getValue().equals(text))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PARSE_INIT_ACTION_ERROR));
  }
}
