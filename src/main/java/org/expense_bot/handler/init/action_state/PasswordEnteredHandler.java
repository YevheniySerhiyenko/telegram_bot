package org.expense_bot.handler.init.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Session;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEnteredHandler implements InitActionState{

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;

  @Override
  public void handle(Long userId) {
    final Session session = sessionService.getSession(userId);
    final String password = session.getPassword();
    userService.getByUserId(userId).ifPresent(user -> {
      final String userPassword = user.getPassword();
      final boolean equals = userPassword.equals(password);
    });
    telegramService.sendMessage(userId, Messages.HELLO_MESSAGE, keyboardBuilder.buildMainMenu());
    telegramService.sendMessage(userId, Messages.CHOOSE_YOUR_ACTION);
    sessionService.updateState(userId, ConversationState.Init.WAITING_INIT_ACTION);
  }

}
