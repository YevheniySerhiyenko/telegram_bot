package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.constant.Command;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.InitAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;

  @Override
  public boolean isApplicable(Request request) {
	return isCommand(request.getUpdate(), Command.START);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	userService.getOptionalUser(userId).ifPresent(user -> {
	  if(user.isEnablePassword() && !user.isLogined()) {
		telegramService.sendMessage(userId, Messages.ENTER_PASSWORD);
		sessionService.update(SessionUtil.getSession(userId, InitAction.PASSWORD));
	  } else {
		extracted(userId);
	  }
	});
  }

  private void extracted(Long userId) {
	telegramService.sendMessage(userId, Messages.CHOOSE_YOUR_ACTION, keyboardBuilder.buildMainMenu());
	sessionService.updateState(userId, ConversationState.Init.WAITING_INIT_ACTION);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }

}
