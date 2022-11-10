package org.expense_bot.handler.setting;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class SettingRequestHandler extends UserRequestHandler {

  private static final String COMMAND = "/settings";

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate(), COMMAND);
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildSettingsMenu();
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	userSessionService.updateState(chatId, ConversationState.Settings.WAITING_SETTINGS_ACTION);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }

}
