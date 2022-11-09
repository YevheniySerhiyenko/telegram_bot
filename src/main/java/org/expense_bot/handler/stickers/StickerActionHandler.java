package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.model.UserSticker;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class StickerActionHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final StickerService stickerService;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final KeyboardHelper keyboardHelper;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextMessage(request.getUpdate())
      && ConversationState.Settings.WAITING_STICKERS_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
    final Long chatId = userRequest.getChatId();
    final String action = userRequest.getUpdate().getMessage().getText();
    final UserSession session = userRequest.getUserSession();
    session.setStickerAction(action);
    final String token = getSticker(chatId, action);
    telegramService.sendSticker(chatId,token);
    telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION,keyboardHelper.buildStickerOptions(action));
    session.setState(ConversationState.Settings.WAITING_STICKERS_FINAL_ACTION);
    userSessionService.saveSession(session);
    handleCallBack(userRequest);
  }

  private void handleCallBack(UserRequest userRequest) {
    //todo
    final CallbackQuery callbackQuery = userRequest.getUpdate().getCallbackQuery();
    final String text = callbackQuery.getMessage().getText();
  }

  private String getSticker(Long chatId, String action) {
    return userStickerService.getOne(action, chatId)
      .map(UserSticker::getToken)
      .orElse(stickerService.getOne(action)
        .map(Sticker::getToken)
        .orElse(null));

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
