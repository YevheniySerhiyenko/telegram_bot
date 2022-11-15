package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.Objects;

import static org.expense_bot.constant.Messages.*;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request,ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleExpensesBackButton(request);
	final Long userId = request.getUserId();
	final String period = getUpdateData(request);
	final boolean anotherMonth = drawAnotherMonthCalendar(request, Calendar.changeMonth(request));
	if(anotherMonth){
	  return;
	}
	checkPeriod(request, period);

	final ReplyKeyboard keyboard = keyboardBuilder.buildCheckCategoriesMenu(userId);
	telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY, keyboard);
	sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_CHECK_CATEGORY));
  }

  private boolean drawAnotherMonthCalendar(Request request, InlineKeyboardMarkup keyboard) {
	if(Objects.nonNull(keyboard)) {
	  telegramService.editKeyboardMarkup(request, keyboard);
	  sessionService.updateState(request.getUserId(), ConversationState.Expenses.WAITING_FOR_PERIOD);
	  return true;
	}
	return false;
  }

  private void checkPeriod(Request request, String period) {
	final Long chatId = request.getUserId();
	if(Objects.equals(period, Period.PERIOD.getValue())) {
	  telegramService.sendMessage(chatId, CHOOSE_PERIOD, Calendar.buildCalendar(LocalDate.now()));
	  sessionService.update(SessionUtil.getSession(chatId, period, ConversationState.Expenses.WAITING_FOR_PERIOD));
	  return;
	}
	if(hasCallBack(request)) {
	  final LocalDate date = Calendar.getDate(request);
	  telegramService.sendMessage(chatId, String.format(DATE, date));
	  sessionService.updateState(chatId, ConversationState.Expenses.WAITING_FOR_PERIOD);
	  setDates(date, request);
	}
  }

  private void setDates(LocalDate date, Request request) {
	final Long chatId = request.getUserId();
	final Session session = request.getSession();
	final LocalDate periodFrom = session.getPeriodFrom();
	final LocalDate periodTo = session.getPeriodTo();
	if(periodFrom == null) {
	  session.setPeriodFrom(date);
	  return;
	}
	if(periodTo == null) {
	  session.setPeriodTo(date);
	}
	if(session.getPeriodFrom() != null && session.getPeriodTo() != null) {
	  session.setState(ConversationState.Expenses.WAITING_CHECK_CATEGORY);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildCheckCategoriesMenu(chatId);
	  telegramService.sendMessage(chatId, CHOOSE_CATEGORY, keyboard);
	  sessionService.update(session);
	  return;
	}
	sessionService.update(session);

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
