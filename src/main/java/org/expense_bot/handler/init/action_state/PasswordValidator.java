package org.expense_bot.handler.init.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Session;
import org.expense_bot.model.User;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements InitActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void handle(Long userId) {
	final Session session = sessionService.getSession(userId);
	final String password = session.getPassword();
	final User user = userService.getUser(userId);
	final boolean validPassword = encoder.matches(password, user.getPassword());
	if(validPassword) {
	  telegramService.sendMessage(userId, Messages.HELLO_MESSAGE, keyboardBuilder.buildMainMenu());
	  telegramService.sendMessage(userId, Messages.CHOOSE_YOUR_ACTION);
	  sessionService.updateState(userId, ConversationState.Init.WAITING_INIT_ACTION);
	  userService.login(userId);
	} else {
	  sessionService.updateState(userId, ConversationState.Init.PASSWORD_ENTER);
	}
  }

}
