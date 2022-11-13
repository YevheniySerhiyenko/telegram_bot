package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
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

@Component
@RequiredArgsConstructor
public class EnterPeriodCheckIncomesHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackButtonHandler backButtonHandler;
  private final IncomeService incomeService;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(Request request) {
    backButtonHandler.handleIncomeBackButton(request);
    final Long chatId = request.getUserId();
    if(hasMessage(request) && Objects.equals(getUpdateData(request), Messages.ENTER_DATE)) {
      telegramService.sendMessage(chatId, Messages.ENTER_DATE, Calendar.buildMonthCalendar(LocalDate.now()));
    }
    if(hasCallBack(request)) {
      final InlineKeyboardMarkup keyboard = Calendar.changeYear(request);
      drawAnotherYearCalendar(request, keyboard);
      final String date = getUpdateData(request);
      final LocalDate monthValue = Calendar.parseMonthYear(date);
      sendIncomesByDate(request.getUserId(), monthValue, date);
      sessionService.updateState(chatId, ConversationState.Incomes.WAITING_FOR_PERIOD);
    }
  }

  private void sendIncomesByDate(Long chatId, LocalDate date, String monthYear) {
    final List<Income> incomes = incomeService.getAll(chatId, date);
    if(Objects.isNull(incomes) || incomes.isEmpty()) {
      telegramService.sendMessage(chatId, Messages.NO_INCOMES_FOR_PERIOD + monthYear);
      throw new RuntimeException(Messages.NO_INCOMES_FOR_PERIOD);
    }
    incomes.forEach(income -> telegramService.sendMessage(chatId, IncomeUtil.getIncome(income)));
  }

  private void drawAnotherYearCalendar(Request userRequest, InlineKeyboardMarkup keyboard) {
    if(Objects.nonNull(keyboard)) {
      telegramService.editKeyboardMarkup(userRequest, keyboard);
      sessionService.updateState(userRequest.getUserId(), ConversationState.Incomes.WAITING_FOR_PERIOD);
      throw new RuntimeException("Waiting another year");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }


}
