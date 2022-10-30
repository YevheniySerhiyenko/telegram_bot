package org.expense_bot.handler.income;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Income;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IncomeActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;
  private final IncomeService incomeService;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_ENTERED_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final IncomeAction incomeAction = userRequest.getUserSession().getIncomeAction();

	switch (incomeAction) {
	  case CHECK:
	  case CHECK_BALANCE:
		break;
	  case WRITE:
		final Double sum = Double.parseDouble(userRequest.getUpdate().getMessage().getText());
		incomeService.save(chatId, sum, LocalDateTime.now());
		telegramService.sendMessage(chatId, Messages.SUCCESS_INCOME);
		break;
	}

	final UserSession userSession = userRequest.getUserSession();
	userSession.setState(ConversationState.WAITING_INCOME_ACTION);
	userSessionService.saveSession(chatId, userSession);

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}