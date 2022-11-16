package org.expense_bot.handler;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.model.Request;
import org.expense_bot.model.Sticker;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserStickerService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NewUserHandler {

  private final UserRepository userRepository;
  private final TelegramService telegramService;
  private final UserStickerService userStickerService;
  private final StickerSender stickerSender;
  private final StickerService stickerService;

  public void handle(Request request)  {

	final Long userId = request.getUserId();
	stickerSender.sendSticker(userId,StickerAction.HELLO.name());
	telegramService.sendMessage(userId, Messages.HELLO);
	try {
	  Thread.sleep(5000);
	} catch (InterruptedException e) {
	  e.printStackTrace();
	}
	telegramService.sendMessage(userId, "/categories");
	//set all stickers to user
	final List<Sticker> all = stickerService.getAll();
	all.forEach(sticker -> userStickerService.save(userId,sticker.getAction(),sticker.getToken()));
  }

  public void sendCategoriesInfo(Request request) {

  }

  public void sendIncomesInfo(Request request) {

  }

  public void sendExpensesInfo(Request request) {

  }

  public void sendSettingsInfo(Request request) {

  }

}
