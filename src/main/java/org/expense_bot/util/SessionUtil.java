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

  public static Session getSession(Long userId, CategoryAction categoryAction) {
	return Session.builder()
	  .userId(userId)
	  .categoryAction(categoryAction)
	  .state(ConversationState.Categories.WAITING_FINAL_ACTION)
	  .build();
  }

  public static Session getSession(Long userId, String period, ConversationState state) {
	return Session.builder()
	  .userId(userId)
	  .period(period)
	  .state(state)
	  .build();
  }

  public static Session getSession(BigDecimal sum, Long userId) {
	return Session.builder()
	  .userId(userId)
	  .expenseSum(sum)
	  .state(ConversationState.Init.WAITING_EXPENSE_ACTION)
	  .build();
  }

  public static Session buildExpenseSession(Long userId, LocalDate date) {
	return Session.builder()
	  .userId(userId)
	  .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
	  .expenseDate(date)
	  .build();
  }

  public static Session getSession(Long userId, List<Expense> expenses, String category) {
	return Session.builder()
	  .userId(userId)
	  .expenseList(expenses)
	  .category(category)
	  .state(ConversationState.Expenses.WAITING_ADDITIONAL_ACTION)
	  .build();
  }

  public static Session buildSession(Long userId, LocalDate localDate) {
	return Session.builder()
	  .userId(userId)
	  .expenseDate(localDate)
	  .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
	  .build();
  }

  public static Session buildSession(Long userId, String category) {
	return Session.builder()
	  .userId(userId)
	  .category(category)
	  .state(ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM)
	  .build();
  }

  public static Session getSession(Long userId) {
	return Session.builder()
	  .userId(userId)
	  .incomeAction(IncomeAction.WRITE)
	  .state(ConversationState.Incomes.WAITING_FOR_INCOME_SUM)
	  .build();
  }

  public static Session getSession(Long userId, IncomeAction action) {
	return Session.builder()
	  .userId(userId)
	  .incomeAction(action)
	  .build();
  }

  public static Session buildIncomeSession(Long userId, LocalDate localDate) {
	return Session.builder()
	  .userId(userId)
	  .incomeDate(localDate)
	  .state(ConversationState.Incomes.WAITING_FOR_INCOME_SUM)
	  .build();
  }

  public static Session getSession(Long userId, StickerAction action) {
	return Session.builder()
	  .userId(userId)
	  .stickerAction(action)
	  .state(ConversationState.Settings.WAITING_STICKERS_FINAL_ACTION)
	  .build();
  }

  public static Session getStickerSession(Long userId, String action) {
	return Session.builder()
	  .userId(userId)
	  .action(action)
	  .state(ConversationState.Settings.WAITING_STICKERS_ACTION)
	  .build();
  }

  public static Session finalUpdate(Long userId) {
	return Session.builder()
	  .userId(userId)
	  .periodFrom(null)
	  .periodTo(null)
	  .state(ConversationState.Expenses.WAITING_ADDITIONAL_ACTION)
	  .build();
  }

}
