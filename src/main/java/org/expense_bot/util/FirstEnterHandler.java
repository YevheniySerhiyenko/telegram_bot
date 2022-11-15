package org.expense_bot.util;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.Request;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirstEnterHandler {

  private final TelegramService telegramService;
  private final StickerService stickerService;
  private final SessionService sessionService;

  public void firstEnterHandle(Request userRequest) {
	final Long userId = userRequest.getUserId();
//	stickerService.
	//todo
	String token = "CAACAgIAAxkBAAEGRjFjYPXCrp0yRZdeOjiCZ1o5rvO9QAACGQAD6dgTKFdhEtpsYKrLKgQ";
	telegramService.sendMessage(userId, Messages.HELLO);
	telegramService.sendSticker(userId,token);

  }

  public void sendCategoriesInfo(Request userRequest){
    // send info about default categories
    //set ConversationState Started
  }

  public void sendStickersInfo(Request userRequest){
    //send info about Sticker Actions
	//send info about edit
	// send info about Settings
  }
}
