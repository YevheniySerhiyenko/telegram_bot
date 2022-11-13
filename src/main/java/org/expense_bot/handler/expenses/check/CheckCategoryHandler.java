package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.mapper.ExpenseMapper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
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
  public void handle(Request userRequest) {
	backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboard keyboard = keyboardBuilder.buildCreatePDFMenu();
	final Long chatId = userRequest.getUserId();
	final String category = getUpdateData(userRequest);
	final List<Expense> expenses = getExpenses(chatId, category);
	final String period = sessionService.getSession(chatId).getPeriod();
	sessionService.update(SessionUtil.getSession(chatId, expenses, category));
	sendExpenses(expenses, keyboard, chatId, period);
  }

  private void sendExpenses(List<Expense> expenses, ReplyKeyboard keyboard, Long chatId, String period) {
	if(expenses == null || expenses.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NO_EXPENSES_FOR_PERIOD, keyboardBuilder.buildExpenseMenu());
	  sessionService.updateState(chatId,ConversationState.Init.WAITING_EXPENSE_ACTION);
	} else {
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  final List<ExpenseGroup> groupList = ExpenseMapper.toGroup(expenses);
	  groupList.forEach(expenseGroup -> telegramService.sendMessage(chatId, ExpenseUtil.getMessage(expenseGroup), keyboardBuilder.buildExpenseOptions(expenseGroup)));
	  telegramService.sendMessage(chatId, ExpenseUtil.getSumMessage(expenses, period), keyboardBuilder.buildCreatePDFMenu());
	}
  }

  private List<Expense> getExpenses(Long chatId, String category) {
	final Session session = sessionService.getSession(chatId);
	final String period = session.getPeriod();
	switch (period) {
	  case Messages.DAY:
		return expenseService.getByOneDay(chatId, category);
	  case Messages.WEEK:
		return expenseService.getByOneWeek(chatId, category);
	  case Messages.MONTH:
		return expenseService.getByOneMonth(chatId, category);
	  case Messages.PERIOD:
		final LocalDate periodFrom = session.getPeriodFrom();
		final LocalDate periodTo = session.getPeriodTo();
		return expenseService.getByPeriod(chatId, periodFrom, periodTo, category);
	  default:
		return null;
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
