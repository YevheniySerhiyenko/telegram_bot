package org.expense_bot.handler.setting;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Command;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
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
