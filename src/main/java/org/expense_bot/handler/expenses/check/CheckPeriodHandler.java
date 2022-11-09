package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.UserSessionUtil;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final BackButtonHandler backButtonHandler;
  private final UserSessionUtil userSessionUtil;

  @Override
  public boolean isApplicable(UserRequest request) {
	return ConversationState.Expenses.WAITING_FOR_PERIOD.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleExpensesBackButton(userRequest);
	final Long chatId = userRequest.getChatId();
	final ReplyKeyboardMarkup keyboard = keyboardHelper.buildCheckCategoriesMenu(chatId);
	String period = Utils.getUpdateData(userRequest);
	checkPeriod(userRequest, period);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY, keyboard);
	userSessionService.saveSession(getSession(chatId, period, ConversationState.Expenses.WAITING_CHECK_CATEGORY));
  }


  private void checkPeriod(UserRequest userRequest, String period) {
	final Long chatId = userRequest.getChatId();
	if(period != null && period.equals(Period.PERIOD.getValue())) {
	  telegramService.sendMessage(chatId, Messages.CHOOSE_PERIOD, Calendar.buildCalendar(LocalDate.now()));
	  userSessionService.updateSession(getSession(chatId, period, ConversationState.Expenses.WAITING_FOR_PERIOD));
	  throw new RuntimeException(Messages.CHOOSE_PERIOD);
	}
	if(Utils.hasCallBack(userRequest)) {
	  final LocalDate date = Calendar.getDate(userRequest);
	  telegramService.sendMessage(chatId, String.format(Messages.DATE, date));

	  userSessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_PERIOD);
	  setDates(date, userRequest);
	}
  }

  private void setDates(LocalDate date, UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final UserSession session = userRequest.getUserSession();
	final LocalDate periodFrom = session.getPeriodFrom();
	final LocalDate periodTo = session.getPeriodTo();
	if(periodFrom == null) {
	  session.setPeriodFrom(date);
	}
	if(periodFrom != null && periodTo == null) {
	  session.setPeriodTo(date);
	}
	if(session.getPeriodFrom() != null && session.getPeriodTo() != null) {
	  session.setState(ConversationState.Expenses.WAITING_CHECK_CATEGORY);
	  final ReplyKeyboardMarkup keyboard = keyboardHelper.buildCheckCategoriesMenu(chatId);
	  telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY, keyboard);
	}
	userSessionUtil.update(session);

	throw new RuntimeException("Dates entered");
  }

  private UserSession getSession(Long chatId, String period, ConversationState state) {
	return UserSession.builder()
	  .chatId(chatId)
	  .period(period)
	  .state(state)
	  .build();
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
