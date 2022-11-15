package org.expense_bot.handler.init.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitIncomesHandler implements InitActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
    sessionService.updateState(userId, ConversationState.Init.WAITING_INCOME_ACTION);
    telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboardBuilder.buildIncomeMenu());
  }

}
