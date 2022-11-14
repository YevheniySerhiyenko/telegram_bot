package org.expense_bot.handler.categories.action_state;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.expense_bot.constant.Messages;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryAddNewHandler implements CategoryActionState {

  @Autowired
  private TelegramService telegramService;
  @Autowired
  private KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
	telegramService.sendMessage(userId, Messages.ENTER_CATEGORY_NAME, keyboardBuilder.buildBackButton());
  }

}
