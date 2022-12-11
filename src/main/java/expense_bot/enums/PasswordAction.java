package expense_bot.enums;

import expense_bot.constant.ErrorMessages;
import expense_bot.handler.settings.password.password_action.PasswordActionState;
import expense_bot.handler.settings.password.password_action.PasswordChangeHandler;
import expense_bot.handler.settings.password.password_action.PasswordDisableHandler;
import expense_bot.handler.settings.password.password_action.PasswordSetHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PasswordAction {
  UPDATE_PASSWORD(Button.UPDATE_PASSWORD.getValue(), PasswordChangeHandler.class),
  SET_PASSWORD(Button.SET_PASSWORD.getValue(), PasswordSetHandler.class),
  DISABLE_PASSWORD(Button.DISABLE_PASSWORD.getValue(), PasswordDisableHandler.class);

  private final String value;
  private final Class<? extends PasswordActionState> handler;

  public static PasswordAction parseAction(String text) {
    return Arrays.stream(values())
      .filter(action -> action.getValue().equals(text))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PARSE_PASSWORD_ACTION_ERROR));
  }
}
