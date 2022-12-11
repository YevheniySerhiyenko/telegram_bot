package expense_bot.handler.expenses.write;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class CategoryEnteredHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_CATEGORY);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final ReplyKeyboard keyboard = keyboardBuilder.buildSetDateMenu();
    telegramService.sendMessage(userId, Messages.ENTER_SUM, keyboard);
    final String category = getUpdateData(request);
    sessionService.update(SessionUtil.buildSession(userId, category));
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
