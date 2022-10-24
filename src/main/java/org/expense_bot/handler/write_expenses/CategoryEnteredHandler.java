package org.expense_bot.handler.write_expenses;

import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class CategoryEnteredHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;

  public CategoryEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
	this.telegramService = telegramService;
	this.keyboardHelper = keyboardHelper;
	this.userSessionService = userSessionService;
  }

  public boolean isApplicable(UserRequest userRequest) {
	return this.isTextMessage(userRequest.getUpdate()) && ConversationState.WAITING_FOR_CATEGORY.equals(userRequest.getUserSession().getState());
  }

  public void handle(UserRequest userRequest) {
	ReplyKeyboardMarkup replyKeyboardMarkup = this.keyboardHelper.buildMenuWithCancel();
	this.telegramService.sendMessage(userRequest.getChatId(), "✍️Введи суму витрат⤵️", replyKeyboardMarkup);
	String category = userRequest.getUpdate().getMessage().getText();
	UserSession session = userRequest.getUserSession();
	session.setCategory(category);
	session.setState(ConversationState.WAITING_FOR_SUM);
	this.userSessionService.saveSession(userRequest.getChatId(), session);
  }

  public boolean isGlobal() {
	return false;
  }

}
