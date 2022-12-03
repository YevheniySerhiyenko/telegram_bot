package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.action_state.PasswordEnteredHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.impl.SessionService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHandler extends RequestHandler {

  private final ApplicationContext context;
  private final SessionService sessionService;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request,ConversationState.Init.PASSWORD_ENTERED);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();

	final String password = getUpdateData(request);
	sessionService.update(Session.builder().userId(userId).password(password).build());
	context.getBean(PasswordEnteredHandler.class).handle(userId);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }

}
