package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Income;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.IncomeUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeRequestHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isEqual(request, ConversationState.Init.WAITING_INCOME_ACTION);
  }

  @Override
  public void handle(UserRequest request) {
    backButtonHandler.handleMainMenuBackButton(request);
    final Long chatId = request.getChatId();
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
    userSessionService.updateState(chatId, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  private void handleWriteIncomes(Long chatId) {
    telegramService.sendMessage(chatId, Messages.ENTER_INCOME_SUM, keyboardBuilder.buildSetDateMenu());
    userSessionService.update(SessionUtil.getSession(chatId));
  }

  private void handleCheckBalance(Long chatId, List<Income> incomes) {
    final List<Expense> expenses = expenseService.getByOneMonth(chatId, Messages.BY_ALL_CATEGORIES);
    final BigDecimal allExpensesSum = ExpenseUtil.getSum(expenses);
    final BigDecimal allIncomesSum = IncomeUtil.getSum(incomes);

    final BigDecimal actualBalance = allIncomesSum.subtract(allExpensesSum);

    telegramService.sendMessage(chatId, Messages.ALL_EXPENSES_SUM + allExpensesSum);
    telegramService.sendMessage(chatId, Messages.ALL_INCOMES_SUM + allIncomesSum);
    telegramService.sendMessage(chatId, Messages.ACTUAL_BALANCE + actualBalance);
    userSessionService.update(SessionUtil.getSession(chatId,IncomeAction.WRITE));
  }


  @Override
  public boolean isGlobal() {
    return false;
  }
}
