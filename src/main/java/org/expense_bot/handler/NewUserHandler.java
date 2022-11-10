package org.expense_bot.handler;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.UserRequest;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewUserHandler {

  private final UserRepository userRepository;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final StickerSender stickerSender;

  public void handle(UserRequest userRequest)  {

	final Long chatId = userRequest.getChatId();
	//todo
	String token = "CAACAgIAAxkBAAEGRjFjYPXCrp0yRZdeOjiCZ1o5rvO9QAACGQAD6dgTKFdhEtpsYKrLKgQ";
	telegramService.sendMessage(chatId, Messages.HELLO);
	try {
	  Thread.sleep(5000);
	} catch (InterruptedException e) {
	  e.printStackTrace();
	}
	telegramService.sendSticker(chatId, token);
	telegramService.sendMessage(chatId, "/categories");

  }

  public void sendCategoriesInfo(UserRequest request) {

  }

  public void sendIncomesInfo(UserRequest request) {

  }

  public void sendExpensesInfo(UserRequest request) {

  }

  public void sendSettingsInfo(UserRequest request) {

  }

}
