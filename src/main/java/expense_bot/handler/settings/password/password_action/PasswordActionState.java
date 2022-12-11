package expense_bot.handler.settings.password.password_action;

import expense_bot.model.Request;

public interface PasswordActionState {

  void init(Long userId);

  void handle(Request request);

  void handleFinal(Request request);

}
