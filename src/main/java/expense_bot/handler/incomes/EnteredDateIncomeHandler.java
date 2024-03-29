package expense_bot.handler.incomes;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.Calendar;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EnteredDateIncomeHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleIncomeBackButton(request)) {
      return;
    }
    final Optional<InlineKeyboardMarkup> keyboard = Calendar.changeMonth(request);
    final Long userId = request.getUserId();
    final boolean anotherMonth = drawAnotherMonth(request, keyboard);
    if (anotherMonth) {
      return;
    }
      final LocalDate localDate = getLocalDate(request, keyboard);
      sessionService.update(SessionUtil.buildIncomeSession(userId, localDate));
      telegramService.sendMessage(userId, String.format(Messages.DATE, localDate));
  }

  private LocalDate getLocalDate(Request request, Optional<InlineKeyboardMarkup> keyboard) {
    return keyboard.isEmpty() ? Calendar.getDate(request) : null;
  }

  private boolean drawAnotherMonth(Request request, Optional<InlineKeyboardMarkup> keyboard) {
    if (keyboard.isEmpty()) {
      return false;
    }
    telegramService.editKeyboardMarkup(request, keyboard.get());
    sessionService.updateState(request.getUserId(), ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
    return true;
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
