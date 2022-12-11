package expense_bot.handler.expenses.check;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.enums.Period;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.Calendar;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final String period = getUpdateData(request);
    final boolean showCalendar = checkRequest(userId, period);
    if (showCalendar) {
      return;
    }
    final ReplyKeyboard keyboard = keyboardBuilder.buildCheckCategoriesMenu(userId);
    sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_CHECK_CATEGORY));
    telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY, keyboard);
  }


  private boolean checkRequest(Long userId, String period) {
    if (!Objects.equals(period, Period.PERIOD.getValue())) {
      return false;
    }
    telegramService.sendMessage(userId, Messages.CHOOSE_PERIOD, Calendar.buildCalendar(LocalDate.now()));
    sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_FOR_TWO_DATES));
    return true;
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
