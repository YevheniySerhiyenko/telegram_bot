package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.constant.Command;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.InitAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.User;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final UserService userService;

  @Override
  public boolean isApplicable(Request request) {
	return isCommand(request.getUpdate(), Command.START);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	final Optional<User> user = userService.getByUserId(userId);
	user.ifPresent(value -> {
	  if(true) {
		telegramService.sendMessage(userId, Messages.ENTER_PASSWORD);
		sessionService.update(SessionUtil.getSession(userId,InitAction.PASSWORD));
	  }
	});
  }

  @Override
  public boolean isGlobal() {
	return true;
  }

}
