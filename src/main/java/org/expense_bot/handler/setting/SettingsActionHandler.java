package org.expense_bot.handler.setting;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.model.UserSticker;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SettingsActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final UserStickerService userStickerService;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_SETTINGS_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final String action = userRequest.getUpdate().getMessage().getText();
	final UserSession session = userRequest.getUserSession();
	session.setAction(action);
	handleAction(chatId);
	session.setState(ConversationState.WAITING_CHANGE_SETTING_ACTION);
	userSessionService.saveSession(chatId, session);
  }

  private void handleAction(Long chatId) {
	final List<String> userStickers = userStickerService.getAll(chatId)
	  .stream()
	  .map(UserSticker::getAction)
	  .collect(Collectors.toList());
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(userStickers);
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION_TO_SHOW_STICKER,replyKeyboardMarkup);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
