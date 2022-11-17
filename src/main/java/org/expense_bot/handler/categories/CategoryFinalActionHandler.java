package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFinalActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final BackHandler backHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Categories.WAITING_FINAL_ACTION);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleCategoriesBackButton(request)) {
	  return;
	}
	final Long userId = request.getUserId();
	final String param = getUpdateData(request);
	final CategoryAction categoryAction = sessionService.getSession(userId).getCategoryAction();
	context.getBean(categoryAction.getHandler()).handleFinal(userId, param);
	sessionService.update(SessionUtil.getSession(userId, categoryAction));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
