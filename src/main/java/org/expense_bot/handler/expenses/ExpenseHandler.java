package org.expense_bot.handler.expenses;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseHandler extends UserRequestHandler {

  private final UserSessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserCategoryService userCategoryService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.Init.WAITING_EXPENSE_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
    backButtonHandler.handleMainMenuBackButton(userRequest);
	final String text = Utils.getUpdateData(userRequest);
	final Long chatId = userRequest.getChatId();

	switch (text){
	  case Messages.CHECK_EXPENSES:
		telegramService.sendMessage(chatId, Messages.CHOOSE_PERIOD, keyboardHelper.buildCheckPeriodMenu());
		sessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_PERIOD);
		break;
	  case Messages.WRITE_EXPENSES:
		final List<UserCategory> byUserId = userCategoryService.getByUserId(chatId);
		checkEmpty(chatId, byUserId);
		telegramService.sendMessage(chatId, Messages.CHOOSE_EXPENSES_CATEGORY, keyboardHelper.buildCategoriesMenu(chatId));
		sessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_CATEGORY);
		break;
	}
  }


  private void checkEmpty(Long chatId, List<UserCategory> byUserId) {
	if(byUserId.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NO_ONE_CATEGORY_FOUND, keyboardHelper.buildBackButtonMenu());
	  throw new RuntimeException(Messages.NOTHING_TO_DELETE);
	}
  }
  @Override
  public boolean isGlobal() {
	return false;
  }

}