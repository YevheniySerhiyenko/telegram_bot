package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionCommandHandler extends UserRequestHandler {

  private final UserSessionService userSessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Init.WAITING_INIT_ACTION);
  }

  @Override
  public void handle(UserRequest request) {
	backButtonHandler.handleMainMenuBackButton(request);
	final Long chatId = request.getChatId();
	final String initAction = getUpdateData(request);
	switch (initAction) {
	  case Messages.EXPENSES:
		userSessionService.updateState(chatId, ConversationState.Init.WAITING_EXPENSE_ACTION);
		telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboardBuilder.buildExpenseMenu());
		break;
	  case Messages.INCOMES:
		userSessionService.updateState(chatId, ConversationState.Init.WAITING_INCOME_ACTION);
		telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboardBuilder.buildIncomeMenu());
		break;
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
