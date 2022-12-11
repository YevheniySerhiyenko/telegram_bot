package expense_bot.handler.incomes.action_state;

import expense_bot.model.Request;

public interface IncomeActionState {

  void handle(Long userId);

  void handleFinal(Request request);

}
