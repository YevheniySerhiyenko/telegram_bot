package org.expense_bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.handler.stickers.action_state.ChangeStickerHandler;
import org.expense_bot.handler.stickers.action_state.DisableStickerHandler;
import org.expense_bot.handler.stickers.action_state.StickerEditActionState;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StickerEditAction {

  OFF(Messages.OFF, DisableStickerHandler.class),
  CHANGE(Messages.CHANGE, ChangeStickerHandler.class);

  private final String value;
  private final Class<? extends StickerEditActionState> handler;

  public static StickerEditAction parseAction(String text) {
	return Arrays.stream(values())
	  .filter(action -> action.getValue().equals(text))
	  .findFirst()
	  .orElseThrow(() -> new IllegalArgumentException("Unable to parse sticker edit action"));

  }
}
