package org.expense_bot.handler.setting.password.password_action;

import org.expense_bot.model.Request;

public interface PasswordActionState {

  void init(Long userId);

  void handle(Request request);

  void handleFinal(Request request);

}
