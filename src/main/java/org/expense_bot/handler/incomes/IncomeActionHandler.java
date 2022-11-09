package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.UserSessionUtil;
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
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;
  private final UserSessionUtil userSessionUtil;
  
  private static final LocalDateTime NOW = LocalDateTime.now();

  @Override
  public boolean isApplicable(UserRequest request) {
	return ConversationState.Incomes.WAITING_FOR_SUM.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	userSessionUtil.checkEnteredDate(userRequest,ConversationState.Incomes.WAITING_FOR_ANOTHER_DATE);
	backButtonHandler.handleIncomeBackButton(userRequest);
	final Long chatId = userRequest.getChatId();
	final IncomeAction incomeAction = userRequest.getUserSession().getIncomeAction();

	switch (incomeAction) {
	  case WRITE:
		final BigDecimal sum = new BigDecimal(userRequest.getUpdate().getMessage().getText());
		final LocalDate incomeDate = userRequest.getUserSession().getIncomeDate();
		incomeService.save(chatId, sum, incomeDate == null ? NOW : incomeDate.atStartOfDay());
		telegramService.sendMessage(chatId, Messages.SUCCESS);
		telegramService.sendMessage(chatId, Messages.SUCCESS_INCOME, keyboardHelper.buildIncomeMenu());
		break;
	}

	final UserSession userSession = userRequest.getUserSession();
	userSession.setState(ConversationState.Init.WAITING_INCOME_ACTION);
	userSessionService.saveSession(userSession);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
