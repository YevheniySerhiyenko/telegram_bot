package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IncomeActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final IncomeService incomeService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  private static final LocalDateTime NOW = LocalDateTime.now();

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Incomes.WAITING_FOR_INCOME_SUM);
  }

  @Override
  public void handle(UserRequest userRequest) {
	userSessionService.checkEnteredDate(userRequest, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE, this.getClass());
	backButtonHandler.handleIncomeBackButton(userRequest);
	final Long chatId = userRequest.getChatId();
	final IncomeAction incomeAction = userRequest.getUserSession().getIncomeAction();

	switch (incomeAction) {
	  case WRITE:
		final BigDecimal sum = new BigDecimal(getUpdateData(userRequest));
		final LocalDate incomeDate = userRequest.getUserSession().getIncomeDate();
		incomeService.save(chatId, sum, incomeDate == null ? NOW : incomeDate.atStartOfDay());
		telegramService.sendMessage(chatId, Messages.SUCCESS);
		telegramService.sendMessage(chatId, Messages.SUCCESS_INCOME, keyboardBuilder.buildIncomeMenu());
		break;
	}
	userSessionService.updateState(chatId, ConversationState.Init.WAITING_INCOME_ACTION);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
