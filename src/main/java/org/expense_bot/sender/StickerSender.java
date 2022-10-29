package org.expense_bot.sender;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserSticker;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StickerSender {

  private final StickerService stickerService;
  private final UserStickerService userStickerService;

  public String getSticker(Long userId, String action) {
	return userStickerService.getOne(action, userId)
	  .map(UserSticker::getToken)
	  .orElseGet(() -> stickerService.getOne(action)
		.map(Sticker::getToken)
		.orElse(null));
  }

}
