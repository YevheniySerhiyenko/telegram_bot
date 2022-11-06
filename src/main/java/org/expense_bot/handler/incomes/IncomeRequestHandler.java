package org.expense_bot.handler.incomes;

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
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeRequestHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextMessage(request.getUpdate())
      && ConversationState.Init.WAITING_INCOME_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
    backButtonHandler.handleMainMenuBackButton(userRequest);
    final Long chatId = userRequest.getChatId();
    final String incomeAction = userRequest.getUpdate().getMessage().getText();
    final UserSession userSession = userRequest.getUserSession();
    final List<Income> incomes = incomeService.getAllCurrentMonth(chatId);
    switch (incomeAction) {
      case Messages.WRITE_INCOMES:
        handleWriteIncomes(chatId, userSession);
        break;
      case Messages.CHECK_INCOMES:
        checkIncomes(chatId, incomes, userSession, userRequest);
        break;
      case Messages.CHECK_BALANCE:
        handleCheckBalance(chatId, incomes, userSession);
        break;
    }
  }

  private void checkIncomes(Long chatId, List<Income> incomes, UserSession userSession, UserRequest userRequest) {
    incomes.forEach(income -> telegramService.sendMessage(chatId, getIncome(income)));
    telegramService.sendMessage(chatId, Messages.CHOOSE_PERIOD, keyboardHelper.buildSetDateMenu());
    userSession.setState(ConversationState.Incomes.WAITING_FOR_PERIOD);
    if(userRequest.getUpdate().hasCallbackQuery()) {
      userSession.setState(ConversationState.Incomes.WAITING_FOR_PERIOD);
    } else {
      final Month month = userRequest.getUserSession().getIncomePeriod();
      incomeService.getAll(chatId, month);
    }
  }

  private void handleWriteIncomes(Long chatId, UserSession userSession) {
    userSession.setState(ConversationState.Incomes.WAITING_FOR_SUM);
    telegramService.sendMessage(chatId, Messages.ENTER_INCOME_SUM, keyboardHelper.buildSetDateMenu());
    userSession.setIncomeAction(IncomeAction.WRITE);
    userSessionService.saveSession(chatId, userSession);
  }

  private void handleCheckBalance(Long chatId, List<Income> incomes, UserSession userSession) {
    final BigDecimal allExpensesSum = getAllExpensesSum(chatId);

    final BigDecimal allIncomesSum = incomes.stream()
      .map(Income::getSum)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal actualBalance = allIncomesSum.subtract(allExpensesSum);

    telegramService.sendMessage(chatId, Messages.ALL_EXPENSES_SUM + allExpensesSum);
    telegramService.sendMessage(chatId, Messages.ALL_INCOMES_SUM + allIncomesSum);
    telegramService.sendMessage(chatId, Messages.ACTUAL_BALANCE + actualBalance);
    userSession.setIncomeAction(IncomeAction.WRITE);
    userSessionService.saveSession(chatId, userSession);
  }

  private BigDecimal getAllExpensesSum(Long chatId) {
    final List<Expense> expenses = expenseService.getByOneMonth(chatId, Messages.BY_ALL_CATEGORIES);
    return expenses.stream()
      .map(Expense::getSum)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

  private String getIncome(Income income) {
    return getDate(income.getIncomeDate()) + " - " + income.getSum() + " грн";
  }

  private String getDate(LocalDateTime localDate) {
    return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }
}
