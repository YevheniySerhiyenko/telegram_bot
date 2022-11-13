package org.expense_bot.handler.expenses;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.Request;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseHandler extends RequestHandler {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserCategoryService userCategoryService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Init.WAITING_EXPENSE_ACTION);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleMainMenuBackButton(request);
	final String text = getUpdateData(request);
	final Long chatId = request.getUserId();
	final List<UserCategory> categories = userCategoryService.getByUserId(chatId);
	checkEmpty(chatId, categories);

	switch (text) {
	  case Messages.CHECK_EXPENSES:
		telegramService.sendMessage(chatId, Messages.CHOOSE_PERIOD, keyboardBuilder.buildCheckPeriodMenu());
		sessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_PERIOD);
		break;
	  case Messages.WRITE_EXPENSES:
		telegramService.sendMessage(chatId, Messages.CHOOSE_EXPENSES_CATEGORY, keyboardBuilder.buildCategoriesMenu(chatId));
		sessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_CATEGORY);
		break;
	}
  }


  private void checkEmpty(Long chatId, List<UserCategory> categories) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NO_ONE_CATEGORY_FOUND, keyboardBuilder.buildBackButton());
	  throw new RuntimeException(Messages.NOTHING_TO_DELETE);
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
