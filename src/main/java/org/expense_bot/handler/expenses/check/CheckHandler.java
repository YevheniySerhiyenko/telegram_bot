package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
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
@RequiredArgsConstructor
public class CheckHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;


  public boolean isApplicable(UserRequest userRequest) {
	return isTextMessage(userRequest.getUpdate(), Messages.CHECK_EXPENSES);
  }

  public void handle(UserRequest userRequest) {
	ReplyKeyboardMarkup replyKeyboardMarkup = this.keyboardHelper.buildCheckPeriodMenu();
	this.telegramService.sendMessage(userRequest.getChatId(), Messages.CHOOSE_PERIOD, replyKeyboardMarkup);
	String action = userRequest.getUpdate().getMessage().getText();
	UserSession session = userRequest.getUserSession();
	session.setAction(action);
	session.setState(ConversationState.WAITING_FOR_PERIOD);
	this.userSessionService.saveSession(userRequest.getChatId(), session);
  }

  public boolean isGlobal() {
	return true;
  }
}
