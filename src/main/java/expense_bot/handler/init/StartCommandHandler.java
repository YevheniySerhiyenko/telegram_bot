package expense_bot.handler.init;

import expense_bot.constant.Command;
import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.enums.InitAction;
import expense_bot.handler.RequestHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.UserService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;

  @Override
  public boolean isApplicable(Request request) {
    return isCommand(request.getUpdate(), Command.START);
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    userService.getOptional(userId).ifPresent(user -> {
      if (user.isEnablePassword() && !user.isLogined()) {
        telegramService.sendMessage(userId, Messages.ENTER_PASSWORD);
        sessionService.update(SessionUtil.getSession(userId, InitAction.PASSWORD));
      } else {
        extracted(userId);
      }
    });
  }

  private void extracted(Long userId) {
    telegramService.sendMessage(userId, Messages.CHOOSE_YOUR_ACTION, keyboardBuilder.buildMainMenu());
    sessionService.updateState(userId, ConversationState.Init.WAITING_INIT_ACTION);
  }

  @Override
  public boolean isGlobal() {
    return true;
  }

}
