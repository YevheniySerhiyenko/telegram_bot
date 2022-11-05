package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class BackButtonHandler {

  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;
  private final TelegramService telegramService;

  public void handleCategoriesBackButton(UserRequest userRequest) {
	final String param = userRequest.getUpdate().getMessage().getText();
	if(param.equals(Constants.BUTTON_BACK)){
	  final Long chatId = userRequest.getChatId();
	  final UserSession session = userRequest.getUserSession();
	  session.setState(ConversationState.Categories.WAITING_CATEGORY_ACTION);
	  userSessionService.saveSession(chatId, session);
	  final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCategoryOptionsMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION,replyKeyboardMarkup);
	  throw new RuntimeException("Handle back button");
	}
  }

  public void handleExpensesBackButton(UserRequest userRequest) {
	if(userRequest.getUpdate().hasMessage()) {
	  final String param = userRequest.getUpdate().getMessage().getText();
	  if(param.equals(Constants.BUTTON_BACK)) {
		final Long chatId = userRequest.getChatId();
		final UserSession session = userRequest.getUserSession();
		session.setState(ConversationState.Init.WAITING_EXPENSE_ACTION);
		userSessionService.saveSession(chatId, session);
		final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildExpenseMenu();
		telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, replyKeyboardMarkup);
		throw new RuntimeException("Handle back button in expenses");
	  }
	}
  }

  public void handleMainMenuBackButton(UserRequest userRequest) {
	final String param = userRequest.getUpdate().getMessage().getText();
	if(param.equals(Constants.BUTTON_BACK)){
	  final Long chatId = userRequest.getChatId();
	  final UserSession session = userRequest.getUserSession();
	  session.setState(ConversationState.Init.WAITING_INIT_ACTION);
	  userSessionService.saveSession(chatId, session);
	  final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION,replyKeyboardMarkup);
	  throw new RuntimeException("Handle main menu back button");
	}
  }

  public void handleIncomeBackButton(UserRequest userRequest) {
	final String param = userRequest.getUpdate().getMessage().getText();
	if(param.equals(Constants.BUTTON_BACK)) {
	  final Long chatId = userRequest.getChatId();
	  final UserSession session = userRequest.getUserSession();
	  session.setState(ConversationState.Init.WAITING_INCOME_ACTION);
	  userSessionService.saveSession(chatId, session);
	  final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildIncomeMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, replyKeyboardMarkup);
	  throw new RuntimeException("Handle main menu back button");
	}
  }

  public boolean handleButton(UserRequest userRequest, String state) {
	final String param = userRequest.getUpdate().getMessage().getText();
	final boolean back = param.equals(Constants.BUTTON_BACK);
	if(back) {
	  final Long chatId = userRequest.getChatId();
	  final UserSession session = userSessionService.getSession(chatId);

	  final ConversationState previousState = getPreviousState(state);
	  session.setState(previousState);
	  userSessionService.saveSession(chatId, session);
	}
	return back;
  }

  private ConversationState getPreviousState(String state) {
	return ConversationState.getPreviousState(state);
  }

}
