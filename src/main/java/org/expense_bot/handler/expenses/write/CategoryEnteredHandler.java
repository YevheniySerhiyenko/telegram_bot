package org.expense_bot.handler.expenses.write;

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
public class CategoryEnteredHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest userRequest) {
	return isTextMessage(userRequest.getUpdate())
	  && ConversationState.Expenses.WAITING_FOR_CATEGORY.equals(userRequest.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
    backButtonHandler.handleExpensesBackButton(userRequest);
	final Long chatId = userRequest.getChatId();
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildSetDateMenu();
	telegramService.sendMessage(chatId, Messages.ENTER_SUM, replyKeyboardMarkup);
	final String category = userRequest.getUpdate().getMessage().getText();
	final UserSession session = userRequest.getUserSession();
	session.setCategory(category);
	session.setState(ConversationState.Expenses.WAITING_FOR_SUM);
	userSessionService.saveSession(chatId, session);
  }

  public boolean isGlobal() {
	return false;
  }

}
