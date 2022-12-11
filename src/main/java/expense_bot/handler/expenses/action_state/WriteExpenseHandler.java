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
public class WriteExpenseHandler implements ExpenseActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
    final ReplyKeyboard keyboard = keyboardBuilder.buildCategoriesMenu(userId);
    telegramService.sendMessage(userId, Messages.CHOOSE_EXPENSES_CATEGORY, keyboard);
    sessionService.updateState(userId, ConversationState.Expenses.WAITING_FOR_CATEGORY);
  }

}
