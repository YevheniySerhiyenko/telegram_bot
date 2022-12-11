package expense_bot.handler.init;

import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.action_state.PasswordValidator;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHandler extends RequestHandler {

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
    context.getBean(PasswordValidator.class).handle(userId);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
