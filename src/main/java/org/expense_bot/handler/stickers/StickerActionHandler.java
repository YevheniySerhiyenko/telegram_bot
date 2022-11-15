package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.Request;
import org.expense_bot.model.UserSticker;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class StickerActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final StickerService stickerService;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Settings.WAITING_STICKERS_ACTION);
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    final StickerAction action = StickerAction.valueOf(getUpdateData(request));
    final String token = getSticker(userId, action);
    telegramService.sendSticker(userId,token);
    telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboardBuilder.buildStickerOptions(action.name()));
    sessionService.update(SessionUtil.getSession(userId, action));
    handleCallBack(request);
  }

  private void handleCallBack(Request request) {
    //todo
    final CallbackQuery callbackQuery = request.getUpdate().getCallbackQuery();
    final String text = callbackQuery.getMessage().getText();
  }

  private String getSticker(Long userId, StickerAction action) {
    return userStickerService.getOne(action.name(), userId)
      .map(UserSticker::getToken)
      .orElse(stickerService.getOne(action.name())
        .map(Sticker::getToken)
        .orElse(null));

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
