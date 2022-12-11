package expense_bot.handler.incomes.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.Button;
import expense_bot.enums.IncomeAction;
import expense_bot.enums.Period;
import expense_bot.model.Expense;
import expense_bot.model.Income;
import expense_bot.model.Request;
import expense_bot.service.ExpenseService;
import expense_bot.service.IncomeService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.ExpenseUtil;
import expense_bot.util.IncomeUtil;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeBalanceHandler implements IncomeActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;

  @Override
  public void handle(Long userId) {
    final List<Income> incomes = incomeService.getAllCurrentMonth(userId);
    final LocalDateTime from = Period.MONTH.getDateFrom();
    final LocalDateTime to = Period.MONTH.getDateTo();

    final List<Expense> expenses = expenseService
      .getByPeriod(userId, from, to, Button.BY_ALL_CATEGORIES.getValue());
    final BigDecimal allExpensesSum = ExpenseUtil.getSum(expenses);
    final BigDecimal allIncomesSum = IncomeUtil.getSum(incomes);

    final BigDecimal actualBalance = allIncomesSum.subtract(allExpensesSum);

    telegramService.sendMessage(userId, Messages.ALL_EXPENSES_SUM + allExpensesSum);
    telegramService.sendMessage(userId, Messages.ALL_INCOMES_SUM + allIncomesSum);
    telegramService.sendMessage(userId, Messages.ACTUAL_BALANCE + actualBalance);
    sessionService.update(SessionUtil.getSession(userId, IncomeAction.WRITE));
  }

  @Override
  public void handleFinal(Request request) {
    //do something
  }

}
