package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.InitAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.model.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionCommandHandler extends RequestHandler {

  private final BackButtonHandler backButtonHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Init.WAITING_INIT_ACTION);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleMainMenuBackButton(request);
	final Long userId = request.getUserId();
	final InitAction initAction = InitAction.parseAction(getUpdateData(request));
	context.getBean(initAction.getHandler()).handle(userId);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
