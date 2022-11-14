package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.mapper.ExpenseMapper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
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
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_CHECK_CATEGORY);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleExpensesBackButton(request);
	final Long chatId = request.getUserId();
	final String category = getUpdateData(request);
	final String period = sessionService.getSession(chatId).getPeriod();
	final List<Expense> expenses = getExpenses(chatId, category,period);
	sessionService.update(SessionUtil.getSession(chatId, expenses, category));
	sendExpenses(expenses, chatId, period);
  }

  private List<Expense> getExpenses(Long chatId, String periodParam, String category) {
	final Period period = Period.parsePeriod(periodParam);
	final LocalDateTime from = period.getDateFrom();
	final LocalDateTime to = period.getDateTo();
	return expenseService.getByPeriod(chatId, from, to, category);
  }

  private void sendExpenses(List<Expense> expenses, Long chatId, String period) {
	if(expenses == null || expenses.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NO_EXPENSES_FOR_PERIOD, keyboardBuilder.buildExpenseMenu());
	  sessionService.updateState(chatId,ConversationState.Init.WAITING_EXPENSE_ACTION);
	} else {
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  final List<ExpenseGroup> groupList = ExpenseMapper.toGroup(expenses);
	  groupList.forEach(group ->
		telegramService.sendMessage(chatId, ExpenseUtil.getMessage(group), keyboardBuilder.buildExpenseOptions(group)));
	  telegramService.sendMessage(chatId, ExpenseUtil.getSumMessage(expenses, period), keyboardBuilder.buildCreatePDFMenu());
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
