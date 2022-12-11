package expense_bot.handler.expenses.write;

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
public class EnteredDateHandlerExpense extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final boolean anotherMonth = drawAnotherMonthCalendar(request, Calendar.changeMonth(request));
    if (anotherMonth) {
      return;
    }
    final LocalDate localDate = Calendar.getDate(request);
    sessionService.update(SessionUtil.getSession(userId, localDate));
    telegramService.sendMessage(userId, String.format(Messages.DATE, localDate));
  }

  private boolean drawAnotherMonthCalendar(Request request, Optional<InlineKeyboardMarkup> keyboard) {
    if (keyboard.isEmpty()) {
      return false;
    }
    sessionService.updateState(request.getUserId(), ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
    telegramService.editKeyboardMarkup(request, keyboard.get());
    return true;
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
