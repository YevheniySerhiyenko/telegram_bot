package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

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
  public void handle(UserRequest dispatchRequest) {
    final Long chatId = dispatchRequest.getChatId();
    if(dispatchRequest.getUpdate().hasMessage()){
      final String text = dispatchRequest.getUpdate().getMessage().getText();
      if(text.equals(Messages.ENTER_DATE)){
        telegramService.sendMessage(chatId,Messages.ENTER_DATE,Calendar.buildMonthCalendar(LocalDate.now()));
        throw new RuntimeException("Build month calendar");
      }
    }
    if(dispatchRequest.getUpdate().hasCallbackQuery()){
      final String monthParam = dispatchRequest.getUpdate().getCallbackQuery().getData();
      final Month month = Calendar.parseMonth(monthParam);
      final UserSession userSession = dispatchRequest.getUserSession();
      userSession.setIncomePeriod(month);
      userSession.setState(ConversationState.Incomes.WAITING_INCOME_ACTION);
    }
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
