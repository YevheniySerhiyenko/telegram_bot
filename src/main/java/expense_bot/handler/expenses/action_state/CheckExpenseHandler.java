package expense_bot.handler.expenses.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class CheckExpenseHandler implements ExpenseActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;

  @Override
  public void handle(Long userId) {
    final ReplyKeyboard keyboard = KeyboardBuilder.buildCheckPeriodMenu();
    telegramService.sendMessage(userId, Messages.CHOOSE_PERIOD, keyboard);
    sessionService.updateState(userId, ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

}
