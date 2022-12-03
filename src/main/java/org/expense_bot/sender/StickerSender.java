package org.expense_bot.sender;

import lombok.RequiredArgsConstructor;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StickerSender {

  private final TelegramService telegramService;

  public void sendSticker(Long userId, String action) {
	final String token = getSticker(userId, action);
	if(Objects.nonNull(token) && !token.isEmpty()) {
	  telegramService.sendSticker(userId, token);
	}
  }

  public String getSticker(Long userId, String action) {
	return "get token from message properties by action";
  }

}
