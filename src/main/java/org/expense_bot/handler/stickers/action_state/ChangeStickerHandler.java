package org.expense_bot.handler.stickers.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserSticker;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChangeStickerHandler implements StickerEditActionState{

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final UserStickerService userStickerService;
  @Override
  public void handle(Long userId, String stickerAction) {
//    telegramService.sendMessage(userId,Message);
    final UserSticker sticker = userStickerService.getOne(stickerAction, userId)
      .orElseThrow(() -> new RuntimeException("Unable to find user sticker"));


  }

}
