package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnteredDateHandlerExpense extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleExpensesBackButton(request);
	final InlineKeyboardMarkup keyboard = Calendar.changeMonth(request);
	final Long chatId = request.getUserId();
	drawAnotherMonthCalendar(request, keyboard);
	final LocalDate localDate = Calendar.getDate(request);
	sessionService.update(SessionUtil.buildSession(chatId, localDate));
	telegramService.sendMessage(chatId, String.format(Messages.DATE, localDate));
  }

  private void drawAnotherMonthCalendar(Request request, InlineKeyboardMarkup keyboard) {
	if(Objects.nonNull(keyboard)) {
	  telegramService.editKeyboardMarkup(request, keyboard);
	  sessionService.updateState(request.getUserId(), ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
	  throw new RuntimeException("Waiting another date");
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
