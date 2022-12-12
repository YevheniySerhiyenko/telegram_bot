package expense_bot.handler.expenses.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.UserCategory;
import expense_bot.service.UserCategoryService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WriteExpenseHandler implements ExpenseActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final UserCategoryService userCategoryService;

  @Override
  public void handle(Long userId) {
    final ReplyKeyboard keyboard = KeyboardBuilder.buildCategories(getCategories(userId));
    telegramService.sendMessage(userId, Messages.CHOOSE_EXPENSES_CATEGORY, keyboard);
    sessionService.updateState(userId, ConversationState.Expenses.WAITING_FOR_CATEGORY);
  }

  private List<String> getCategories(Long userId) {
    return userCategoryService.getByUserId(userId)
      .stream()
      .map(UserCategory::getCategory)
      .collect(Collectors.toList());
  }

}
