package org.expense_bot.handler.categories.action_state;

public interface CategoryActionState {

  void handle(Long userId);

  void handleFinal(Long userId, String categoryParam);

}
