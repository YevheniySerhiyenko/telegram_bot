package expense_bot.handler.settings.password;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.model.User;
import expense_bot.service.UserService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class PasswordSettingsRequestHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Settings.WAITING_SETTINGS_ACTION);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleBackPasswordSetting(request)) {
      return;
    }

    final Long userId = request.getUserId();
    final User user = userService.getUser(userId);
    final boolean enablePassword = user.isEnablePassword();
    final ReplyKeyboard keyboard = keyboardBuilder.buildPasswordOptions(enablePassword);

    if (enablePassword) {
      telegramService.sendMessage(userId, Messages.ALREADY_HAD_PASSWORD, keyboard);
    } else {
      telegramService.sendMessage(userId, Messages.NO_PASSWORD, keyboard);
    }
    sessionService.update(SessionUtil.getSession(userId, enablePassword));
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
