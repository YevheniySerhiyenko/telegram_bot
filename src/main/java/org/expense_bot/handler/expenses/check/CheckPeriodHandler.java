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
public class CheckPeriodHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.Expenses.WAITING_FOR_PERIOD.equals(request.getUserSession().getState());

  }

  @Override
  public void handle(UserRequest userRequest) {
    backButtonHandler.handleExpensesBackButton(userRequest);
	final Long chatId = userRequest.getChatId();
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCheckCategoriesMenu(chatId);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY, replyKeyboardMarkup);
	final String period = userRequest.getUpdate().getMessage().getText();
	final UserSession session = userRequest.getUserSession();
	session.setPeriod(period);
	session.setState(ConversationState.Expenses.WAITING_CHECK_CATEGORY);
	userSessionService.saveSession(chatId, session);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
