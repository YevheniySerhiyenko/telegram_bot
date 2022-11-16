package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StickerActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final StickerService stickerService;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final KeyboardBuilder keyboardBuilder;
  private final StickerSender stickerSender;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Settings.WAITING_STICKERS_ACTION);
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    final StickerAction action = StickerAction.valueOf(getUpdateData(request));
    stickerSender.sendSticker(userId, action.name());
    telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboardBuilder.buildStickerOptions(action.name()));
    sessionService.update(SessionUtil.getSession(userId, action));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
