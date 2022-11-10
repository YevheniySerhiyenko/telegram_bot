package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.ExpenseAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdditionalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	//edit state
	return isEqual(request, ConversationState.Expenses.WAITING_CHECK_CATEGORY);
  }

  @Override
  public void handle(UserRequest request) {
    backButtonHandler.handleExpensesBackButton(request);
//	if(!hasCallBack(request)) {
//	  final String action = getUpdateData(request);
//	  switch (action){
//		case ExpenseAction.EDIT.name():
//		  handleEdit();
//		  break;
//		default:
//		  throw new IllegalStateException("Unexpected value: " + action);
//	  }
//	}
  }

  private void handleEdit() {

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
