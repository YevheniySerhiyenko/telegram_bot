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
public class PasswordChangeHandler implements PasswordActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void init(Long userId) {
    final ReplyKeyboard keyboard = keyboardBuilder.buildBackButton();
    telegramService.sendMessage(userId, Messages.ENTER_OLD_PASSWORD, keyboard);
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    final String oldPassword = sessionService.get(userId).getPassword();
    final User user = userService.getUser(userId);

    final ReplyKeyboard keyboard = keyboardBuilder.buildBackButton();
    final boolean validPassword = encoder.matches(oldPassword, user.getPassword());
    if (validPassword) {
      telegramService.sendMessage(userId, Messages.ENTER_NEW_PASSWORD, keyboard);
      sessionService.updateState(userId, ConversationState.Settings.WAITING_FINAL_PASSWORD_ACTION);
    } else {
      telegramService.sendMessage(userId, Messages.WRONG_PASSWORD, keyboard);
      telegramService.sendMessage(userId, Messages.ENTER_OLD_PASSWORD, keyboard);
      sessionService.updateState(userId, ConversationState.Settings.WAITING_PASSWORD_ACTION);
    }
  }

  @Override
  public void handleFinal(Request request) {
    final Long userId = request.getUserId();
    final Session session = sessionService.get(userId);
    final String newPassword = session.getPasswordConfirmed();
    userService.updatePassword(userId, encoder.encode(newPassword), true);
    final ReplyKeyboard keyboard = keyboardBuilder.buildPasswordOptions(true);
    telegramService.sendMessage(userId, Messages.SUCCESS);
    telegramService.sendMessage(userId, Messages.PASSWORD_CHANGED, keyboard);
  }

}
