package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CheckCategoryHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return ConversationState.Expenses.WAITING_CHECK_CATEGORY.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboardMarkup keyboard = keyboardHelper.buildExpenseMenu();
	final Long chatId = userRequest.getChatId();
	final String category = userRequest.getUpdate().getMessage().getText();
	final String period = userSessionService.getSession(chatId).getPeriod();
	userSessionService.updateSession(getSession(chatId, category));
	sendExpenses(keyboard, chatId, period);
  }

  private UserSession getSession(Long chatId, String category) {
	return UserSession.builder()
	  .chatId(chatId)
	  .category(category)
	  .state(ConversationState.Init.WAITING_EXPENSE_ACTION)
	  .build();
  }

  private void sendExpenses(ReplyKeyboardMarkup keyboard, Long chatId, String period) {
	final List<Expense> expenses = getExpenses(chatId);
	if(expenses == null || expenses.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NO_EXPENSES_FOR_PERIOD, keyboard);
	} else {
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  expenses.forEach(expense -> telegramService.sendMessage(chatId, getMessage(expense)));
	  telegramService.sendMessage(chatId, getSumMessage(expenses, period), keyboard);
	}
  }

  private String getSumMessage(List<Expense> expenses, String period) {
	final BigDecimal sum = expenses.stream().map(Expense::getSum).reduce(BigDecimal::add).orElse(null);
	return String.format(Messages.RESPONSE_MESSAGE, period.toLowerCase(Locale.ROOT)) + sum;
  }

  private String getMessage(Expense expense) {
	return expense.getCategory() + " : " + expense.getSum();
  }

  private List<Expense> getExpenses(Long chatId) {
	final UserSession session = userSessionService.getSession(chatId);
	final String period = session.getPeriod();
	final String category = session.getCategory();
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
