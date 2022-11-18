package org.expense_bot.sender;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.expense_bot.handler.RequestHandler.getUpdateData;

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

// move to util
  public Optional<BigDecimal> checkWrongSum(Request request) {
	try {
	  return Optional.of(new BigDecimal(getUpdateData(request)));
	} catch (NumberFormatException e) {
	  final Long userId = request.getUserId();
	  sendSticker(userId, StickerAction.WRONG_ENTERED_SUM.name());
	  telegramService.sendMessage(userId, Messages.WRONG_SUM_FORMAT);
	}
	return Optional.empty();
  }

}
