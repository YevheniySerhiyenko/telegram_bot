package expense_bot.handler.settings.password.password_action;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.model.Session;
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
public class PasswordSetHandler implements PasswordActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void init(Long userId) {
    final User user = userService.getUser(userId);
    if (!user.isEnablePassword() && user.getPassword() == null) {
      final ReplyKeyboard keyboard = KeyboardBuilder.buildBackButton();
      telegramService.sendMessage(userId, Messages.ENTER_NEW_PASSWORD, keyboard);
    }
  }

  @Override
  public void handle(Request request) {
    telegramService.sendMessage(request.getUserId(), Messages.ENTER_PASSWORD_ONE_MORE);
  }

  @Override
  public void handleFinal(Request request) {
    final Long userId = request.getUserId();
    final Session session = sessionService.get(userId);
    if (isPasswordsEquals(session)) {
      userService.updatePassword(userId, encoder.encode(session.getPassword()), true);
      sessionService.updateState(userId, ConversationState.Settings.WAITING_INIT_PASSWORD_ACTION);
      final ReplyKeyboard keyboard = KeyboardBuilder.buildPasswordOptions(true);
      telegramService.sendMessage(userId, Messages.SUCCESS);
      telegramService.sendMessage(userId, Messages.PASSWORD_CHANGED, keyboard);
    } else {
      final ReplyKeyboard keyboard = KeyboardBuilder.buildBackButton();
      telegramService.sendMessage(userId, Messages.PASSWORDS_NOT_IDENTICAL);
      telegramService.sendMessage(userId, Messages.ENTER_PASSWORD_ONE_MORE, keyboard);
      sessionService.updateState(userId, ConversationState.Settings.WAITING_FINAL_PASSWORD_ACTION);
    }
  }

  private boolean isPasswordsEquals(Session session) {
    return session.getPassword().equals(session.getPasswordConfirmed());
  }

}
