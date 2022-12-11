package expense_bot.sender;

import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StickerSender {

  private final TelegramService telegramService;
  private final MessageSource messageSource;

  public void sendSticker(Long userId, String action) {
//    Optional.of(messageSource.getMessage(action, new Object[0], null))
//          .ifPresent(token -> telegramService.sendSticker(userId, token));
  }

}
