package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request,ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	final CategoryAction categoryAction = CategoryAction.parseAction(getUpdateData(request));
	context.getBean(categoryAction.getHandler()).handle(userId);
	sessionService.update(SessionUtil.getSession(userId, categoryAction));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
