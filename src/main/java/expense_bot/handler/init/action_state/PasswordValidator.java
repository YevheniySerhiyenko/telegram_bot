package expense_bot.handler.init.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Session;
import expense_bot.model.User;
import expense_bot.service.UserService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements InitActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final UserService userService;
  private final PasswordEncoder encoder;

  @Override
  public void handle(Long userId) {
    final Session session = sessionService.get(userId);
    final String password = session.getPassword();
    final User user = userService.getUser(userId);
    final boolean validPassword = encoder.matches(password, user.getPassword());
    if (validPassword) {
      telegramService.sendMessage(userId, Messages.HELLO_MESSAGE, KeyboardBuilder.buildMainMenu());
      telegramService.sendMessage(userId, Messages.CHOOSE_YOUR_ACTION);
      sessionService.updateState(userId, ConversationState.Init.WAITING_INIT_ACTION);
      userService.login(userId);
    } else {
      sessionService.updateState(userId, ConversationState.Init.PASSWORD_ENTER);
    }
  }

}
