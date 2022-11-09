package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StickerRequestHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final UserStickerService userStickerService;
  private final StickerService stickerService;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.Settings.WAITING_SETTINGS_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final String action = userRequest.getUpdate().getMessage().getText();
	final UserSession session = userRequest.getUserSession();
	session.setAction(action);
	handleAction(chatId);
	session.setState(ConversationState.Settings.WAITING_STICKERS_ACTION);
	userSessionService.saveSession(session);
  }

  private void handleAction(Long chatId) {
	final List<Sticker> actualStickers = getActualStickersAction(chatId);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildStickersActionMenu(actualStickers);
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION_TO_SHOW_STICKER,replyKeyboardMarkup);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  public List<Sticker> getActualStickersAction(Long chatId){
	return stickerService.getAll(chatId)
	  .stream()
	  .distinct()
	  .collect(Collectors.toList());
  }

}
