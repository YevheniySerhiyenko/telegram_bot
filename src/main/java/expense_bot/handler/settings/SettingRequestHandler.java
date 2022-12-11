package expense_bot.handler.settings;

import expense_bot.constant.Command;
import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class SettingRequestHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public boolean isApplicable(Request request) {
    return isTextMessage(request.getUpdate(), Command.SETTINGS);
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    final ReplyKeyboard keyboard = keyboardBuilder.buildSettingsMenu();
    telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboard);
    sessionService.updateState(userId, ConversationState.Settings.WAITING_SETTINGS_ACTION);
  }

  @Override
  public boolean isGlobal() {
    return true;
  }

}
