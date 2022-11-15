package org.expense_bot.handler.expenses.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckExpenseHandler implements ExpenseActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
	telegramService.sendMessage(userId, Messages.CHOOSE_PERIOD, keyboardBuilder.buildCheckPeriodMenu());
	sessionService.updateState(userId, ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

}
