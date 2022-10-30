package org.expense_bot.handler.init;

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

@Component
@RequiredArgsConstructor
public class ActionCommandHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_INIT_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final String initAction = userRequest.getUpdate().getMessage().getText();
	switch (initAction) {
	  case Messages.EXPENSES:
		handleExpenses(chatId);
		break;
	  case Messages.INCOMES:
		handleIncomes(chatId);
		break;
	}
  }

  private void handleIncomes(Long chatId) {
	userSessionService.saveSession(chatId, buildSession(chatId, ConversationState.WAITING_INCOME_ACTION));
	telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboardHelper.buildIncomeMenu());
  }

  private void handleExpenses(Long chatId) {
	userSessionService.saveSession(chatId, buildSession(chatId, ConversationState.WAITING_FOR_PERIOD));
	telegramService.sendMessage(chatId,Messages.CHOOSE_ACTION, keyboardHelper.buildExpenseMenu());
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  private UserSession buildSession(Long chatId, ConversationState state) {
	return UserSession.builder()
	  .chatId(chatId)
	  .state(state)
	  .build();
  }

}
