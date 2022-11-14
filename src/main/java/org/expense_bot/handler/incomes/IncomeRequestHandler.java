package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Income;
import org.expense_bot.model.Request;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.IncomeUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeRequestHandler extends RequestHandler {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Init.WAITING_INCOME_ACTION);
  }

  @Override
  public void handle(Request request) {
    backButtonHandler.handleMainMenuBackButton(request);
    final Long chatId = request.getUserId();
    final String incomeAction = getUpdateData(request);
    final List<Income> incomes = incomeService.getAllCurrentMonth(chatId);
    switch (incomeAction) {
      case Messages.WRITE_INCOMES:
        handleWriteIncomes(chatId);
        break;
      case Messages.CHECK_INCOMES:
        checkIncomes(chatId, incomes);
        break;
      case Messages.CHECK_BALANCE:
        handleCheckBalance(chatId, incomes);
        break;
    }
  }

  private void checkIncomes(Long chatId, List<Income> incomes) {
    incomes.forEach(income -> telegramService.sendMessage(chatId, IncomeUtil.getIncome(income)));
    telegramService.sendMessage(chatId, Messages.CHOOSE_ANOTHER_PERIOD, keyboardBuilder.buildSetDateMenu());
    sessionService.updateState(chatId, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  private void handleWriteIncomes(Long chatId) {
    telegramService.sendMessage(chatId, Messages.ENTER_INCOME_SUM, keyboardBuilder.buildSetDateMenu());
    sessionService.update(SessionUtil.getSession(chatId));
  }

  private void handleCheckBalance(Long chatId, List<Income> incomes) {
    final LocalDateTime from = Period.MONTH.getDateFrom();
    final LocalDateTime to = Period.MONTH.getDateTo();

    final List<Expense> expenses = expenseService.getByPeriod(chatId, from, to, Messages.BY_ALL_CATEGORIES);
    final BigDecimal allExpensesSum = ExpenseUtil.getSum(expenses);
    final BigDecimal allIncomesSum = IncomeUtil.getSum(incomes);

    final BigDecimal actualBalance = allIncomesSum.subtract(allExpensesSum);

    telegramService.sendMessage(chatId, Messages.ALL_EXPENSES_SUM + allExpensesSum);
    telegramService.sendMessage(chatId, Messages.ALL_INCOMES_SUM + allIncomesSum);
    telegramService.sendMessage(chatId, Messages.ACTUAL_BALANCE + actualBalance);
    sessionService.update(SessionUtil.getSession(chatId, IncomeAction.WRITE));
  }


  @Override
  public boolean isGlobal() {
    return false;
  }
}
