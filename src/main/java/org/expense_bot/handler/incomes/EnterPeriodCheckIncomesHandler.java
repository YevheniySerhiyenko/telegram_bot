package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Income;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.IncomeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EnterPeriodCheckIncomesHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return ConversationState.Incomes.WAITING_FOR_PERIOD.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest request) {
    backButtonHandler.handleIncomeBackButton(request);
    final Long chatId = request.getChatId();
    if(request.getUpdate().hasMessage()) {
      final String text = request.getUpdate().getMessage().getText();
      if(text.equals(Messages.ENTER_DATE)) {
        telegramService.sendMessage(chatId, Messages.ENTER_DATE, Calendar.buildMonthCalendar(LocalDate.now()));
        throw new RuntimeException("Build month calendar");
      }
    }
    if(request.getUpdate().hasCallbackQuery()) {
      final String monthParam = request.getUpdate().getCallbackQuery().getData();
      final Month monthValue = Calendar.parseMonth(monthParam);
      sendIncomesByMonth(request.getChatId(), monthValue);
      final UserSession userSession = request.getUserSession();
      userSession.setState(ConversationState.Incomes.WAITING_FOR_PERIOD);
    }
  }

  private void sendIncomesByMonth(Long chatId, Month month) {
    final List<Income> incomes = incomeService.getAll(chatId, month);
    if(incomes == null || incomes.isEmpty()) {
      telegramService.sendMessage(chatId, Messages.NO_INCOMES_FOR_PERIOD + month);
      throw new RuntimeException(Messages.NO_INCOMES_FOR_PERIOD);
    }
    incomes.forEach(income -> telegramService.sendMessage(chatId, IncomeUtil.getIncome(income)));
  }

  @Override
  public boolean isGlobal() {
    return false;
  }


}
