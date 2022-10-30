package org.expense_bot.handler.income;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Income;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeFinalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;

  @Override
  public boolean isApplicable(UserRequest request) {
//    return isTextMessage(request.getUpdate())
//      && ConversationState.WAITING_INCOME_ACTION.equals(request.getUserSession().getState());
    return false;
  }

  @Override
  public void handle(UserRequest userRequest) {
    final Long chatId = userRequest.getChatId();


  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  private String getIncome(Income income) {
    return income.getSum() + "-" + income.getIncomeDate();
  }

}
