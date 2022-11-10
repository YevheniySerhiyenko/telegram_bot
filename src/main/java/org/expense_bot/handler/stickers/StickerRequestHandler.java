package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StickerRequestHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final UserStickerService userStickerService;
  private final StickerService stickerService;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Settings.WAITING_SETTINGS_ACTION);
  }

  @Override
  public void handle(UserRequest request) {
	final Long chatId = request.getChatId();
	final String action = getUpdateData(request);
	final List<Sticker> actualStickers = getActualStickersAction(chatId);
	final ReplyKeyboard keyboard = keyboardBuilder.buildStickersActionMenu(actualStickers);
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION_TO_SHOW_STICKER, keyboard);
	userSessionService.update(SessionUtil.getStickerSession(chatId, action));
  }


  @Override
  public boolean isGlobal() {
	return false;
  }

  public List<Sticker> getActualStickersAction(Long chatId) {
	return stickerService.getAll(chatId)
	  .stream()
	  .distinct()
	  .collect(Collectors.toList());
  }

}
