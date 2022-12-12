package expense_bot.handler.categories;

import expense_bot.constant.Command;
import expense_bot.constant.Messages;
import expense_bot.handler.RequestHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static expense_bot.enums.ConversationState.Categories.WAITING_CATEGORY_ACTION;

@Component
@RequiredArgsConstructor
public class CategoryRequestHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;

  @Override
  public boolean isApplicable(Request request) {
    return isTextMessage(request.getUpdate(), Command.CATEGORIES);
  }

  @Override
  public void handle(Request request) {
    final Long userId = request.getUserId();
    final ReplyKeyboard keyboard = KeyboardBuilder.buildCategoryOptionsMenu();
    telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboard);
    sessionService.updateState(userId, WAITING_CATEGORY_ACTION);
  }

  @Override
  public boolean isGlobal() {
    return true;
  }

}
