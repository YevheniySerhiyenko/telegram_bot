package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class BackButtonHandler {

  private final UserSessionService userSessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final TelegramService telegramService;

  public void handleCategoriesBackButton(UserRequest request) {
	if(!isBack(request)) {
	  return;
	}
	final Long chatId = request.getChatId();
	userSessionService.updateState(chatId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildCategoryOptionsMenu();
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	throw new RuntimeException("Handle back button in categoris");
  }

  public void handleExpensesBackButton(UserRequest request) {
	if(isBack(request)) {
	  return;
	}
	final Long chatId = request.getChatId();
	userSessionService.updateState(chatId, ConversationState.Init.WAITING_EXPENSE_ACTION);
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildExpenseMenu();
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	throw new RuntimeException("Handle expenses back button");

  }

  public void handleMainMenuBackButton(UserRequest request) {
	if(isBack(request)) {
	  return;
	}
	final Long chatId = request.getChatId();
	userSessionService.updateState(chatId, ConversationState.Init.WAITING_INIT_ACTION);
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildMainMenu();
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	throw new RuntimeException("Handle main menu back button");

  }

  public void handleIncomeBackButton(UserRequest request) {
	if(!isBack(request)) {
	  return;
	}
	final Long chatId = request.getChatId();
	userSessionService.updateState(chatId, ConversationState.Init.WAITING_INCOME_ACTION);
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildIncomeMenu();
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	throw new RuntimeException("Handle incomes back button");

  }

//  public boolean handleButton(UserRequest request, String state) {
//	final String param = request.getUpdate().getMessage().getText();
//	final boolean back = param.equals(Constants.BUTTON_BACK);
//	if(back) {
//	  final Long chatId = request.getChatId();
//	  final UserSession session = userSessionService.getSession(chatId);
//
//	  final ConversationState previousState = getPreviousState(state);
//	  session.setState(previousState);
//	  userSessionService.saveSession(session);
//	}
//	return back;
//  }
//
//  private ConversationState getPreviousState(String state) {
//	return ConversationState.getPreviousState(state);
//  }

  private boolean isBack(UserRequest request) {
	if(!UserRequestHandler.hasMessage(request)) {
	  return false;
	}
	return Objects.equals(UserRequestHandler.getUpdateData(request), Constants.BUTTON_BACK);
  }

}
