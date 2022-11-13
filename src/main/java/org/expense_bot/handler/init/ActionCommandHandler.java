package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionCommandHandler extends RequestHandler {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Init.WAITING_INIT_ACTION);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleMainMenuBackButton(request);
	final Long chatId = request.getUserId();
	final String initAction = getUpdateData(request);
	switch (initAction) {
	  case Messages.EXPENSES:
		sessionService.updateState(chatId, ConversationState.Init.WAITING_EXPENSE_ACTION);
		telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboardBuilder.buildExpenseMenu());
		break;
	  case Messages.INCOMES:
		sessionService.updateState(chatId, ConversationState.Init.WAITING_INCOME_ACTION);
		telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboardBuilder.buildIncomeMenu());
		break;
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
