package org.expense_bot.handler.categories.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryAddNewHandler implements CategoryActionState {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
    telegramService.sendMessage(userId, Messages.ENTER_CATEGORY_NAME, keyboardBuilder.buildBackButton());
  }

}
