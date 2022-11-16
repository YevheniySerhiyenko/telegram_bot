package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class StickerFinalActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final StickerService stickerService;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Settings.WAITING_STICKERS_FINAL_ACTION);
  }

  @Override
  public void handle(Request request) {
    final CallbackQuery callbackQuery = request.getUpdate().getCallbackQuery();
    callbackQuery.getMessage().getText();

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
