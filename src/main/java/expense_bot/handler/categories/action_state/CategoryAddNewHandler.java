package expense_bot.handler.categories.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.service.UserCategoryService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class CategoryAddNewHandler implements CategoryActionState {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final UserCategoryService userCategoryService;


  @Override
  public void handle(Long userId) {
    telegramService.sendMessage(userId, Messages.ENTER_CATEGORY_NAME, keyboardBuilder.buildBackButton());
  }

  @Override
  public void handleFinal(Long userId, String categoryParam) {
    userCategoryService.add(userId, categoryParam);
    final ReplyKeyboard keyboard = keyboardBuilder.buildCategoryOptionsMenu();
    telegramService.sendMessage(userId, Messages.CATEGORY_ADDED_TO_YOUR_LIST, keyboard);
    sessionService.updateState(userId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

}
