package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.Request;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StickerRequestHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final UserStickerService userStickerService;
  private final StickerService stickerService;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Settings.WAITING_SETTINGS_ACTION);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	final String action = getUpdateData(request);
	final List<Sticker> actualStickers = getActualStickersAction(userId);
	final ReplyKeyboard keyboard = keyboardBuilder.buildStickersActionMenu(actualStickers);
	telegramService.sendMessage(userId, Messages.CHOOSE_ACTION_TO_SHOW_STICKER, keyboard);
	sessionService.update(SessionUtil.getStickerSession(userId, action));
  }


  @Override
  public boolean isGlobal() {
	return false;
  }

  public List<Sticker> getActualStickersAction(Long userId) {
	return stickerService.getAll(userId)
	  .stream()
	  .distinct()
	  .collect(Collectors.toList());
  }

}
