package expense_bot.handler;

import expense_bot.constant.Messages;
import expense_bot.enums.StickerAction;
import expense_bot.model.Request;
import expense_bot.repository.UserRepository;
import expense_bot.sender.StickerSender;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewUserHandler {

  private final UserRepository userRepository;
  private final TelegramService telegramService;
  private final StickerSender stickerSender;

  public void handle(Request request) {

    final Long userId = request.getUserId();
    stickerSender.sendSticker(userId, StickerAction.HELLO.name());
    telegramService.sendMessage(userId, Messages.HELLO);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    telegramService.sendMessage(userId, "/categories");
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
