package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.action_state.PasswordEnteredChecker;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordChecker extends RequestHandler {

  private final ApplicationContext context;
  private final SessionService sessionService;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Init.PASSWORD_ENTER);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	final String password = getUpdateData(request);
	sessionService.update(SessionUtil.getSession(password, userId));
	context.getBean(PasswordEnteredChecker.class).handle(userId);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}