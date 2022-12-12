package expense_bot.handler.expenses.check;

import expense_bot.constant.Messages;
import expense_bot.dto.ExpenseGroup;
import expense_bot.enums.ConversationState;
import expense_bot.enums.Period;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.mapper.ExpenseMapper;
import expense_bot.model.Expense;
import expense_bot.model.Request;
import expense_bot.model.Session;
import expense_bot.service.ExpenseService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.ExpenseUtil;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckCategoryHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_CHECK_CATEGORY);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final String category = getUpdateData(request);
    final String period = sessionService.get(userId).getPeriod();
    final List<Expense> expenses = getExpenses(userId, period, category);
    sessionService.update(SessionUtil.getSession(userId, expenses, category));
    sendExpenses(expenses, userId, period);
  }

  private List<Expense> getExpenses(Long userId, String periodParam, String category) {
    final Period period = Period.parsePeriod(periodParam);
    final Session session = sessionService.get(userId);
    final LocalDateTime dateFrom = period.getDateFrom();
    final LocalDateTime dateTo = period.getDateTo();
    final LocalDateTime from = dateFrom == null ? session.getPeriodFrom().atStartOfDay() : dateFrom;
    final LocalDateTime to = dateTo == null ? session.getPeriodTo().atStartOfDay() : dateTo;
    return expenseService.getByPeriod(userId, from, to, category);
  }

  private void sendExpenses(List<Expense> expenses, Long userId, String period) {
    if (expenses == null || expenses.isEmpty()) {
      telegramService.sendMessage(userId, Messages.NO_EXPENSES_FOR_PERIOD, KeyboardBuilder.buildExpenseMenu());
      sessionService.updateState(userId, ConversationState.Init.WAITING_EXPENSE_ACTION);
    } else {
      telegramService.sendMessage(userId, Messages.SUCCESS);
      final List<ExpenseGroup> groupList = ExpenseMapper.toGroup(expenses);
      groupList.forEach(group ->
        telegramService.sendMessage(userId, ExpenseUtil.getMessage(group), KeyboardBuilder.buildExpenseOptions(group)));
      telegramService.sendMessage(userId, ExpenseUtil.getSumMessage(expenses, period), KeyboardBuilder.buildCreatePDFMenu());
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
