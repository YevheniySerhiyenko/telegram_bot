package org.expense_bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.expense_bot.handler.setting.password.password_action.PasswordActionState;
import org.expense_bot.handler.setting.password.password_action.PasswordChangeHandler;
import org.expense_bot.handler.setting.password.password_action.PasswordDisableHandler;
import org.expense_bot.handler.setting.password.password_action.PasswordSetHandler;

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
	  .orElseThrow(() -> new IllegalArgumentException("Unable to parse password action"));
  }
}
