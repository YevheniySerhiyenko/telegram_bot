package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomeActionHandler extends RequestHandler {

  private final SessionService sessionService;
  private final BackHandler backHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_INCOME_SUM);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleIncomeBackButton(request)){
	  return;
	}
	final boolean enteredDate = sessionService.checkEnteredDate(
	  request, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
	if(enteredDate){
	  return;
	}
	final IncomeAction action = request.getSession().getIncomeAction();
	sessionService.updateState(request.getUserId(), ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
	context.getBean(action.getHandler()).handleFinal(request);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
