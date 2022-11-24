package org.expense_bot.handler.incomes.action_state;

import org.expense_bot.model.Request;

public interface IncomeActionState {

  void handle(Long userId);

  void handleFinal(Request request);

}
