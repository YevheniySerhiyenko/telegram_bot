package expense_bot.handler.settings.password;

import expense_bot.enums.ConversationState;
import expense_bot.enums.PasswordAction;
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
public class PasswordActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final BackHandler backHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Settings.WAITING_PASSWORD_ACTION);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleBackPasswordSetting(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final PasswordAction passwordAction = sessionService.get(userId).getPasswordAction();
    final String password = getUpdateData(request);
    sessionService.update(SessionUtil.getSession(userId, passwordAction, password));
    context.getBean(passwordAction.getHandler()).handle(request);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
