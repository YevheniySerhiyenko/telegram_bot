package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.Calendar;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.Objects;

import static org.expense_bot.constant.Messages.DATE;

@Component
@RequiredArgsConstructor
public class CheckAnotherPeriodHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_TWO_DATES);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleExpensesBackButton(request);
	final Long userId = request.getUserId();

	final boolean anotherMonth = drawAnotherMonthCalendar(request, Calendar.changeMonth(request));
	if(anotherMonth){
	  return;
	}
	final boolean enteredPeriod = checkPeriod(request);
	if(enteredPeriod) {
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCheckCategoriesMenu(userId);
	telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY, keyboard);
  }

  private boolean checkPeriod(Request request) {
	final Long userId = request.getUserId();
	if(hasCallBack(request)) {
	  final LocalDate date = Calendar.getDate(request);
	  telegramService.sendMessage(userId, String.format(DATE, date));
//	  sessionService.updateState(userId, ConversationState.Expenses.WAITING_FOR_TWO_DATES);
	  return setDates(date, request);
	}
	return false;
  }

  private boolean setDates(LocalDate date, Request request) {
	final Session session = request.getSession();
	final LocalDate periodFrom = session.getPeriodFrom();
	final LocalDate periodTo = session.getPeriodTo();
	if(periodFrom == null) {
	  session.setPeriodFrom(date);
	  return true;
	}
	if(periodTo == null) {
	  session.setPeriodTo(date);
	}
	if(session.getPeriodFrom() != null && session.getPeriodTo() != null) {
	  session.setState(ConversationState.Expenses.WAITING_CHECK_CATEGORY);
	  sessionService.update(session);
	  return false;
	}
//	sessionService.update(session);

	return false;
  }

  private boolean drawAnotherMonthCalendar(Request request, InlineKeyboardMarkup keyboard) {
	if(Objects.isNull(keyboard)) {
	  return false;
	}
	telegramService.editKeyboardMarkup(request, keyboard);
	sessionService.updateState(request.getUserId(), ConversationState.Expenses.WAITING_FOR_TWO_DATES);
	return true;
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
