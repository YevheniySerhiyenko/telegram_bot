package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

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

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_CHECK_CATEGORY.equals(request.getUserSession().getState());

  }

  @Override
  public void handle(UserRequest userRequest) {
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildExpenseMenu();
	final Long chatId = userRequest.getChatId();
	final String category = userRequest.getUpdate().getMessage().getText();
	final String period = userSessionService.getLastSession(chatId).getPeriod();
	final UserSession session = userRequest.getUserSession();
	session.setCategory(category);
	session.setPeriod(period);
	session.setState(ConversationState.CONVERSATION_STARTED);
	userSessionService.saveSession(chatId, session);
	final List<Expense> expenses = getExpenses(chatId, session.getPeriod(),session.getCategory());
	if(expenses == null || expenses.isEmpty()){
	  telegramService.sendMessage(chatId, Messages.NOT_FOUND_FOR_PERIOD, replyKeyboardMarkup);
	}else {
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  expenses.forEach(expense -> telegramService.sendMessage(chatId, getMessage(expense)));
	  telegramService.sendMessage(chatId, getSumMessage(expenses,period), replyKeyboardMarkup);
	}
  }

  private String getSumMessage(List<Expense> expenses, String period) {
	final Double sum = expenses.stream().map(Expense::getSum).reduce(Double::sum).orElse(null);
	return String.format(Messages.RESPONSE_MESSAGE, period.toLowerCase(Locale.ROOT)) + sum;
  }

  private String getMessage(Expense expense) {
	return expense.getCategory() + " : " + expense.getSum();
  }

  private List<Expense> getExpenses(Long chatId,String period,String category) {
	List<Expense> expenses = new ArrayList<>();
	switch (period) {
	  case Messages.DAY:
		expenses = expenseService.getByOneDay(chatId,category);
		break;
	  case Messages.WEEK:
		expenses = expenseService.getByOneWeek(chatId,category);
		break;
	  case Messages.MONTH:
		expenses = expenseService.getByOneMonth(chatId,category);
		break;
	}
	return expenses;
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
