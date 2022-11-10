package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.model.Income;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.IncomeUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnterPeriodCheckIncomesHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final BackButtonHandler backButtonHandler;
  private final IncomeService incomeService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isEqual(request, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(UserRequest request) {
    backButtonHandler.handleIncomeBackButton(request);
    final Long chatId = request.getChatId();
    if(hasMessage(request)) {
      final String text = getUpdateData(request);
      if(Objects.equals(text, Messages.ENTER_DATE)) {
        telegramService.sendMessage(chatId, Messages.ENTER_DATE, Calendar.buildMonthCalendar(LocalDate.now()));
      }
    }
    if(hasCallBack(request)) {
      final InlineKeyboardMarkup keyboard = Calendar.changeYear(request);
      drawAnotherYearCalendar(request, keyboard);
      final String date = getUpdateData(request);
      final String id = request.getUpdate().getCallbackQuery().getId();
      AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder().callbackQueryId(id).text("HELLO").showAlert(true).build();
      telegramService.sendAnswer(answerCallbackQuery);
      final LocalDate monthValue = Calendar.parseMonthYear(date);
      sendIncomesByDate(request.getChatId(), monthValue, date);
      userSessionService.updateState(chatId, ConversationState.Incomes.WAITING_FOR_PERIOD);
    }
  }

  private void sendIncomesByDate(Long chatId, LocalDate date, String monthYear) {
    final List<Income> incomes = incomeService.getAll(chatId, date);
    if(incomes == null || incomes.isEmpty()) {
      telegramService.sendMessage(chatId, Messages.NO_INCOMES_FOR_PERIOD + monthYear);
      throw new RuntimeException(Messages.NO_INCOMES_FOR_PERIOD);
    }
    incomes.forEach(income -> telegramService.sendMessage(chatId, IncomeUtil.getIncome(income)));
  }

  private void drawAnotherYearCalendar(UserRequest userRequest, InlineKeyboardMarkup keyboard) {
    if(Objects.nonNull(keyboard)) {
      telegramService.editKeyboardMarkup(userRequest, keyboard);
      userSessionService.updateState(userRequest.getChatId(), ConversationState.Incomes.WAITING_FOR_PERIOD);
      throw new RuntimeException("Waiting another year");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }


}
