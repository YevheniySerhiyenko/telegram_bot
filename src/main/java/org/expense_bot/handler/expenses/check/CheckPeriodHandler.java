package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request,ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(UserRequest request) {
	backButtonHandler.handleExpensesBackButton(request);
	final Long chatId = request.getChatId();
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildCheckCategoriesMenu(chatId);
	final String period = getUpdateData(request);
	checkPeriod(request, period);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY, keyboard);
	userSessionService.update(SessionUtil.getSession(chatId, period, ConversationState.Expenses.WAITING_CHECK_CATEGORY));
  }


  private void checkPeriod(UserRequest request, String period) {
	final Long chatId = request.getChatId();
	if(Objects.equals(period, Period.PERIOD.getValue())) {
	  telegramService.sendMessage(chatId, Messages.CHOOSE_PERIOD, Calendar.buildCalendar(LocalDate.now()));
	  userSessionService.update(SessionUtil.getSession(chatId, period, ConversationState.Expenses.WAITING_FOR_PERIOD));
	  throw new RuntimeException(Messages.CHOOSE_PERIOD);
	}
	if(hasCallBack(request)) {
	  final LocalDate date = Calendar.getDate(request);
	  telegramService.sendMessage(chatId, String.format(Messages.DATE, date));

	  userSessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_PERIOD);
	  setDates(date, request);
	}
  }

  private void setDates(LocalDate date, UserRequest request) {
	final Long chatId = request.getChatId();
	final UserSession session = request.getUserSession();
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
	  final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildCheckCategoriesMenu(chatId);
	  telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY, keyboard);
	}
	userSessionService.update(session);

	throw new RuntimeException("Dates entered");
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
