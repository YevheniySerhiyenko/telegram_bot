package expense_bot.handler.categories;

import expense_bot.enums.CategoryAction;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Categories.WAITING_CATEGORY_ACTION);
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
