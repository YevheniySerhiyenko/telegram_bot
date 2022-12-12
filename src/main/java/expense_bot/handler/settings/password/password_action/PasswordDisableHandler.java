package expense_bot.handler.settings.password.password_action;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.model.User;
import expense_bot.service.UserService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class PasswordDisableHandler implements PasswordActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void init(Long userId) {
    final User user = userService.getUser(userId);
    if (user.isEnablePassword() && user.getPassword() != null) {
      final ReplyKeyboard keyboard = KeyboardBuilder.buildBackButton();
      telegramService.sendMessage(userId, Messages.ENTER_OLD_PASSWORD, keyboard);
    }
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    final String oldPassword = sessionService.get(userId).getPassword();
    final User user = userService.getUser(userId);
    final boolean validPassword = encoder.matches(oldPassword, user.getPassword());
    if (validPassword) {
      userService.updatePassword(userId, null, false);
      final ReplyKeyboard keyboard = KeyboardBuilder.buildPasswordOptions(false);
      telegramService.sendMessage(userId, Messages.PASSWORD_DISABLED, keyboard);
      sessionService.updateState(userId, ConversationState.Settings.WAITING_INIT_PASSWORD_ACTION);
    } else {
      telegramService.sendMessage(userId, Messages.WRONG_PASSWORD);
      telegramService.sendMessage(userId, Messages.ENTER_OLD_PASSWORD, KeyboardBuilder.buildBackButton());
      sessionService.updateState(userId, ConversationState.Settings.WAITING_PASSWORD_ACTION);
    }
  }

  @Override
  public void handleFinal(Request request) {
    final Long userId = request.getUserId();
    final boolean enablePassword = sessionService.get(userId).isEnablePassword();
    final ReplyKeyboard keyboard = KeyboardBuilder.buildPasswordOptions(enablePassword);
    telegramService.sendMessage(userId, Messages.PASSWORD_DISABLED, keyboard);
  }

}
