package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.model.Income;
import org.expense_bot.model.Request;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.IncomeUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EnterPeriodCheckIncomesHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackHandler backHandler;
  private final IncomeService incomeService;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(Request request) {
    if(backHandler.handleIncomeBackButton(request)){
      return;
    }
    final Long userId = request.getUserId();
    if(hasMessage(request) && Objects.equals(getUpdateData(request), Buttons.ENTER_DATE.getValue())) {
      telegramService.sendMessage(userId, Buttons.ENTER_DATE.getValue(), Calendar.buildYear(LocalDate.now()));
    }
    if(hasCallBack(request)) {
      final boolean anotherYear = drawAnotherYearCalendar(request, Calendar.changeYear(request));
      if(anotherYear){
        return;
      }
      final String date = getUpdateData(request);
      final LocalDate monthValue = Calendar.parseMonthYear(date);
      sendIncomesByDate(userId, monthValue, date);
      sessionService.updateState(userId, ConversationState.Incomes.WAITING_FOR_PERIOD);
    }
  }

  private void sendIncomesByDate(Long userId, LocalDate date, String monthYear) {
    final List<Income> incomes = incomeService.getAll(userId, date);
    if(Objects.isNull(incomes) || incomes.isEmpty()) {
      telegramService.sendMessage(userId, Messages.NO_INCOMES_FOR_PERIOD + monthYear);
      return;
    }
    incomes.forEach(income -> telegramService.sendMessage(userId, IncomeUtil.getIncome(income)));
  }

  private boolean drawAnotherYearCalendar(Request request, Optional<InlineKeyboardMarkup> keyboard) {
    if(keyboard.isEmpty()) {
      return false;
    }
    telegramService.editKeyboardMarkup(request, keyboard.get());
    sessionService.updateState(request.getUserId(), ConversationState.Incomes.WAITING_FOR_PERIOD);
    return true;
  }

  @Override
  public boolean isGlobal() {
    return false;
  }


}
