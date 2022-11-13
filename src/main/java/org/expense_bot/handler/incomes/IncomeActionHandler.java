package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.IncomeUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IncomeActionHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final IncomeService incomeService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  private static final LocalDateTime NOW = LocalDateTime.now();

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_INCOME_SUM);
  }

  @Override
  public void handle(Request userRequest) {
	sessionService.checkEnteredDate(userRequest, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE, this.getClass());
	backButtonHandler.handleIncomeBackButton(userRequest);
	final Long chatId = userRequest.getUserId();
	final IncomeAction action = userRequest.getSession().getIncomeAction();

	switch (action) {
	  case WRITE:
		final BigDecimal sum = new BigDecimal(getUpdateData(userRequest));
		final LocalDate incomeDate = userRequest.getSession().getIncomeDate();
		incomeService.save(IncomeUtil.buildIncome(chatId,sum,incomeDate));
		telegramService.sendMessage(chatId, Messages.SUCCESS);
		telegramService.sendMessage(chatId, Messages.SUCCESS_INCOME, keyboardBuilder.buildIncomeMenu());
		break;
	}
	sessionService.updateState(chatId, ConversationState.Init.WAITING_INCOME_ACTION);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
