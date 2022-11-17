package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.mapper.ExpenseMapper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckCategoryHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_CHECK_CATEGORY);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleExpensesBackButton(request)) {
	  return;
	}
	final Long userId = request.getUserId();
	final String category = getUpdateData(request);
	final String period = sessionService.getSession(userId).getPeriod();
	final List<Expense> expenses = getExpenses(userId, period, category);
	sessionService.update(SessionUtil.getSession(userId, expenses, category));
	sendExpenses(expenses, userId, period);
  }

  private List<Expense> getExpenses(Long userId, String periodParam, String category) {
	final Period period = Period.parsePeriod(periodParam);
	final Session session = sessionService.getSession(userId);
	final LocalDateTime dateFrom = period.getDateFrom();
	final LocalDateTime dateTo = period.getDateTo();
	final LocalDateTime from = dateFrom == null ? session.getPeriodFrom().atStartOfDay() : dateFrom;
	final LocalDateTime to = dateTo == null ? session.getPeriodTo().atStartOfDay() : dateTo;
	return expenseService.getByPeriod(userId, from, to, category);
  }

  private void sendExpenses(List<Expense> expenses, Long userId, String period) {
	if(expenses == null || expenses.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.NO_EXPENSES_FOR_PERIOD, keyboardBuilder.buildExpenseMenu());
	  sessionService.updateState(userId, ConversationState.Init.WAITING_EXPENSE_ACTION);
	} else {
	  telegramService.sendMessage(userId, Messages.SUCCESS);
	  final List<ExpenseGroup> groupList = ExpenseMapper.toGroup(expenses);
	  groupList.forEach(group ->
		telegramService.sendMessage(userId, ExpenseUtil.getMessage(group), keyboardBuilder.buildExpenseOptions(group)));
	  telegramService.sendMessage(userId, ExpenseUtil.getSumMessage(expenses, period), keyboardBuilder.buildCreatePDFMenu());
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
