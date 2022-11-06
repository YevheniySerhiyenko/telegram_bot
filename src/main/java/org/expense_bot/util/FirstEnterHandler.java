package org.expense_bot.util;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirstEnterHandler {

  private final TelegramService telegramService;
  private final StickerService stickerService;
  private final UserSessionService userSessionService;

  public void firstEnterHandle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
//	stickerService.
	//todo
	String token = "CAACAgIAAxkBAAEGRjFjYPXCrp0yRZdeOjiCZ1o5rvO9QAACGQAD6dgTKFdhEtpsYKrLKgQ";
	telegramService.sendMessage(chatId, Messages.HELLO);
	telegramService.sendSticker(chatId,token);

  }

  public void sendCategoriesInfo(UserRequest userRequest){
    // send info about default categories
    //set Conversation Started
  }

  public void sendStickersInfo(UserRequest userRequest){
    //send info about Sticker Actions
	// send info about Settings
  }
}
