package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class StickerFinalActionHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final StickerService stickerService;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final KeyboardHelper keyboardHelper;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextMessage(request.getUpdate())
      && ConversationState.WAITING_STICKERS_FINAL_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
    final CallbackQuery callbackQuery = userRequest.getUpdate().getCallbackQuery();
    callbackQuery.getMessage().getText();

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}