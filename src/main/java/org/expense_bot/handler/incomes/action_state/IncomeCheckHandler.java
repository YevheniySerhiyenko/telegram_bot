package org.expense_bot.handler.incomes.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Income;
import org.expense_bot.model.Request;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.IncomeUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeCheckHandler implements IncomeActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final IncomeService incomeService;

  @Override
  public void handle(Long userId) {
	final List<Income> incomes = incomeService.getAllCurrentMonth(userId);
	incomes.forEach(income -> telegramService.sendMessage(userId, IncomeUtil.getIncome(income)));
	telegramService.sendMessage(userId, Messages.CHOOSE_ANOTHER_PERIOD, keyboardBuilder.buildSetDateMenu());
	sessionService.updateState(userId, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  @Override
  public void handleFinal(Request request) {
	// do something
  }

}
