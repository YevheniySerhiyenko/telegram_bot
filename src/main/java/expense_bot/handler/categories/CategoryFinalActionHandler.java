package expense_bot.handler.categories;

import expense_bot.enums.CategoryAction;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
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
    if (backHandler.handleCategoriesBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final String param = getUpdateData(request);
    final CategoryAction categoryAction = sessionService.get(userId).getCategoryAction();
    context.getBean(categoryAction.getHandler()).handleFinal(userId, param);
    sessionService.update(SessionUtil.getSession(userId, categoryAction));
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
