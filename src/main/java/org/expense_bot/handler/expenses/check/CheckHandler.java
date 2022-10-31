package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
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
  private final BackButtonHandler backButtonHandler;


  public boolean isApplicable(UserRequest userRequest) {
	return isTextMessage(userRequest.getUpdate(), Messages.CHECK_EXPENSES);
  }

  public void handle(UserRequest userRequest) {
    backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCheckPeriodMenu();
	telegramService.sendMessage(userRequest.getChatId(), Messages.CHOOSE_PERIOD, replyKeyboardMarkup);
	final String action = userRequest.getUpdate().getMessage().getText();
	final UserSession session = userRequest.getUserSession();
	session.setAction(action);
	session.setState(ConversationState.WAITING_FOR_PERIOD);
	userSessionService.saveSession(userRequest.getChatId(), session);
  }

  public boolean isGlobal() {
	return true;
  }
}
