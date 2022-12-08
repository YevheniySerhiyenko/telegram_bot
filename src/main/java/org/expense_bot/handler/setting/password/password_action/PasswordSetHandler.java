package org.expense_bot.handler.setting.password.password_action;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.model.User;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class PasswordSetHandler implements PasswordActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void initHandle(Long userId) {
	final User user = userService.getUser(userId);
	if(!user.isEnablePassword() && user.getPassword() == null) {
	  final ReplyKeyboard keyboard = keyboardBuilder.buildBackButton();
	  telegramService.sendMessage(userId, Messages.ENTER_NEW_PASSWORD, keyboard);
	}
  }

  @Override
  public void handle(Request request) {
	telegramService.sendMessage(request.getUserId(), Messages.ENTER_PASSWORD_ONE_MORE);
  }

  @Override
  public void handleFinal(Request request) {
	final Long userId = request.getUserId();
	final Session session = sessionService.getSession(userId);
	if(isPasswordsEquals(session)) {
	  userService.updatePassword(userId, encoder.encode(session.getPassword()), true);
	  sessionService.updateState(userId, ConversationState.Settings.WAITING_INIT_PASSWORD_ACTION);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildPasswordOptions(true);
	  telegramService.sendMessage(userId, Messages.SUCCESS);
	  telegramService.sendMessage(userId, Messages.PASSWORD_CHANGED, keyboard);
	} else {
	  final ReplyKeyboard keyboard = keyboardBuilder.buildBackButton();
	  telegramService.sendMessage(userId, Messages.PASSWORDS_NOT_IDENTICAL);
	  telegramService.sendMessage(userId, Messages.ENTER_PASSWORD_ONE_MORE, keyboard);
	  sessionService.updateState(userId, ConversationState.Settings.WAITING_FINAL_PASSWORD_ACTION);
	}
  }

  private boolean isPasswordsEquals(Session session) {
	return session.getPassword().equals(session.getPasswordConfirmed());
  }

}
