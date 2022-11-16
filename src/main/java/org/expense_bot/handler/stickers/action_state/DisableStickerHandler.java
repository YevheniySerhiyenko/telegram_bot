package org.expense_bot.handler.stickers.action_state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisableStickerHandler implements StickerEditActionState{

  @Override
  public void handle(Long userId, String stickerAction) {

  }

}
