package expense_bot.handler.init.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitExpenseHandler implements InitActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
    sessionService.updateState(userId, ConversationState.Init.WAITING_EXPENSE_ACTION);
    telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboardBuilder.buildExpenseMenu());
  }

}
