package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckCategoryHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Expenses.WAITING_CHECK_CATEGORY);
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboard keyboard = keyboardBuilder.buildCreatePDFMenu();
	final Long chatId = userRequest.getChatId();
	final String category = getUpdateData(userRequest);
	final List<Expense> expenses = getExpenses(chatId, category);
	final String period = userSessionService.getSession(chatId).getPeriod();
	userSessionService.update(SessionUtil.getSession(chatId, expenses, category));
	sendExpenses(expenses, keyboard, chatId, period);
  }

  private void sendExpenses(List<Expense> expenses, ReplyKeyboard keyboard, Long chatId, String period) {
	if(expenses == null || expenses.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NO_EXPENSES_FOR_PERIOD, keyboard);
	} else {
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  expenses.forEach(expense -> telegramService.sendMessage(chatId, ExpenseUtil.getMessage(expense), keyboardBuilder.buildExpenseOptions(expense.getId())));
	  telegramService.sendMessage(chatId, ExpenseUtil.getSumMessage(expenses, period), keyboard);
	}
  }

  private List<Expense> getExpenses(Long chatId, String category) {
	final UserSession session = userSessionService.getSession(chatId);
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
