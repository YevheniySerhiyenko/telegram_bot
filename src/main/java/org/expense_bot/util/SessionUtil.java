package org.expense_bot.util;

import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SessionUtil {

  public static Session getSession(Long chatId, CategoryAction categoryAction) {
	return Session.builder()
	  .chatId(chatId)
	  .categoryAction(categoryAction)
	  .state(ConversationState.Categories.WAITING_FINAL_ACTION)
	  .build();
  }

  public static Session getSession(Long chatId, String period, ConversationState state) {
	return Session.builder()
	  .chatId(chatId)
	  .period(period)
	  .state(state)
	  .build();
  }

  public static Session getSession(BigDecimal sum, Long chatId) {
	return Session.builder()
	  .chatId(chatId)
	  .expenseSum(sum)
	  .state(ConversationState.Init.WAITING_EXPENSE_ACTION)
	  .build();
  }

  public static Session buildExpenseSession(Long chatId, LocalDate date) {
	return Session.builder()
	  .chatId(chatId)
	  .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
	  .expenseDate(date)
	  .build();
  }

  public static Session getSession(Long chatId, List<Expense> expenses, String category) {
	return Session.builder()
	  .chatId(chatId)
	  .expenseList(expenses)
	  .category(category)
	  .state(ConversationState.Expenses.WAITING_ADDITIONAL_ACTION)
	  .build();
  }

  public static Session buildSession(Long chatId, LocalDate localDate) {
	return Session.builder()
	  .chatId(chatId)
	  .expenseDate(localDate)
	  .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
	  .build();
  }

  public static Session buildSession(Long chatId, String category) {
	return Session.builder()
	  .chatId(chatId)
	  .category(category)
	  .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
	  .build();
  }

  public static Session getSession(Long chatId) {
	return Session.builder()
	  .chatId(chatId)
	  .incomeAction(IncomeAction.WRITE)
	  .state(ConversationState.Incomes.WAITING_FOR_INCOME_SUM)
	  .build();
  }

  public static Session getSession(Long chatId, IncomeAction action) {
	return Session.builder()
	  .chatId(chatId)
	  .incomeAction(action)
	  .build();
  }

  public static Session buildIncomeSession(Long chatId, LocalDate localDate) {
	return Session.builder()
	  .chatId(chatId)
	  .incomeDate(localDate)
	  .state(ConversationState.Incomes.WAITING_FOR_INCOME_SUM)
	  .build();
  }

  public static Session getSession(Long chatId, StickerAction action) {
	return Session.builder()
	  .chatId(chatId)
	  .stickerAction(action)
	  .state(ConversationState.Settings.WAITING_STICKERS_FINAL_ACTION)
	  .build();
  }

  public static Session getStickerSession(Long chatId, String action) {
	return Session.builder()
	  .chatId(chatId)
	  .action(action)
	  .state(ConversationState.Settings.WAITING_STICKERS_ACTION)
	  .build();
  }

}
