package org.expense_bot.handler.setting.password;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.PasswordAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordFinalActionHandler extends RequestHandler {

  private final ApplicationContext context;
  private final SessionService sessionService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Settings.WAITING_FINAL_PASSWORD_ACTION);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleBackPasswordSetting(request)) {
	  return;
	}
	final Long userId = request.getUserId();
	final PasswordAction passwordAction = sessionService.getSession(userId).getPasswordAction();
	final String password = getUpdateData(request);
	sessionService.update(SessionUtil.getSession(userId, password));

	context.getBean(passwordAction.getHandler()).handleFinal(request);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
