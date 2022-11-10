package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSticker;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class StickerActionHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final StickerService stickerService;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isEqual(request,ConversationState.Settings.WAITING_STICKERS_ACTION);
  }

  @Override
  public void handle(UserRequest request) {
    final Long chatId = request.getChatId();
    final StickerAction action = StickerAction.valueOf(getUpdateData(request));
    final String token = getSticker(chatId, action);
    telegramService.sendSticker(chatId,token);
    telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboardBuilder.buildStickerOptions(action.name()));
    userSessionService.update(SessionUtil.getSession(chatId, action));
    handleCallBack(request);
  }

  private void handleCallBack(UserRequest request) {
    //todo
    final CallbackQuery callbackQuery = request.getUpdate().getCallbackQuery();
    final String text = callbackQuery.getMessage().getText();
  }

  private String getSticker(Long chatId, StickerAction action) {
    return userStickerService.getOne(action.name(), chatId)
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
