package expense_bot.handler.init;

import expense_bot.enums.ConversationState;
import expense_bot.enums.InitAction;
import expense_bot.handler.RequestHandler;
import expense_bot.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionCommandHandler extends RequestHandler {

  private final BackHandler backHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Init.WAITING_INIT_ACTION);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleMainMenuBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final InitAction initAction = InitAction.parseAction(getUpdateData(request));
    context.getBean(initAction.getHandler()).handle(userId);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
