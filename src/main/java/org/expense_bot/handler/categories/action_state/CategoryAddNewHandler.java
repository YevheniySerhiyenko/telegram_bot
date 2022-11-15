package org.expense_bot.handler.categories.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

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
    telegramService.sendMessage(userId, Messages.CATEGORY_ADDED_TO_YOUR_LIST, keyboardBuilder.buildCategoryOptionsMenu());
    sessionService.updateState(userId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

}
