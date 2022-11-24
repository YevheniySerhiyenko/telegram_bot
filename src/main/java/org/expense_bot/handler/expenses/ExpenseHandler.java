package org.expense_bot.handler.expenses;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.ExpenseAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserCategoryService userCategoryService;
  private final BackHandler backHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Init.WAITING_EXPENSE_ACTION);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleMainMenuBackButton(request)) {
	  return;
	}
	final Long userId = request.getUserId();
	final List<UserCategory> categories = userCategoryService.getByUserId(userId);
	final ExpenseAction action = ExpenseAction.parse(getUpdateData(request));
	if(checkEmpty(userId, categories)) {
	  return;
	}
	context.getBean(action.getHandler()).handle(userId);
  }

  private boolean checkEmpty(Long userId, List<UserCategory> categories) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.NO_ONE_CATEGORY_FOUND, keyboardBuilder.buildBackButton());
	}
	return categories.isEmpty();
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
