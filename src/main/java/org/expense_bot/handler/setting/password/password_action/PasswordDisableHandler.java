package org.expense_bot.handler.setting.password.password_action;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.exception.UserNotFoundException;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.model.User;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class PasswordDisableHandler implements PasswordActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void initHandle(Long userId) {
	final User user = userService.getByUserId(userId)
	  .orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND + userId));
	if(user.isEnablePassword() && user.getPassword() != null) {
	  final ReplyKeyboard keyboard = keyboardBuilder.buildBackButton();
	  telegramService.sendMessage(userId, Messages.ENTER_OLD_PASSWORD, keyboard);
	}
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	final String oldPassword = sessionService.getSession(userId).getPassword();
	final User user = userService.getByUserId(userId)
	  .orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND + userId));
	final boolean validPassword = encoder.matches(oldPassword, user.getPassword());
	if(validPassword) {
	  userService.disablePassword(userId);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildPasswordOptions(false);
	  telegramService.sendMessage(userId, Messages.PASSWORD_DISABLED, keyboard);
	  sessionService.updateState(userId, ConversationState.Settings.WAITING_INIT_PASSWORD_ACTION);
	} else {
	  telegramService.sendMessage(userId, Messages.WRONG_PASSWORD);
	  telegramService.sendMessage(userId, Messages.ENTER_OLD_PASSWORD, keyboardBuilder.buildBackButton());
	  sessionService.updateState(userId, ConversationState.Settings.WAITING_PASSWORD_ACTION);
	}
  }

  @Override
  public void handleFinal(Request request) {
	final Long userId = request.getUserId();
	final boolean enablePassword = sessionService.getSession(userId).isEnablePassword();
	final ReplyKeyboard keyboard = keyboardBuilder.buildPasswordOptions(enablePassword);
	telegramService.sendMessage(userId, Messages.PASSWORD_DISABLED, keyboard);
  }

}
