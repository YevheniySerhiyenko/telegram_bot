package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.Objects;

import static org.expense_bot.constant.Messages.CHOOSE_PERIOD;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleExpensesBackButton(request)) {
	  return;
	}
	final Long userId = request.getUserId();
	final String period = getUpdateData(request);
	final boolean showCalendar = checkRequest(userId, period);
	if(showCalendar) {
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCheckCategoriesMenu(userId);
	sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_CHECK_CATEGORY));
	telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY, keyboard);
  }


  private boolean checkRequest(Long userId, String period) {
	if(!Objects.equals(period, Period.PERIOD.getValue())) {
	  return false;
	}
	telegramService.sendMessage(userId, CHOOSE_PERIOD, Calendar.buildCalendar(LocalDate.now()));
	sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_FOR_TWO_DATES));
	return true;
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
