package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnteredDateHandlerExpense extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
  }

  @Override
  public void handle(UserRequest request) {
	backButtonHandler.handleExpensesBackButton(request);
	final InlineKeyboardMarkup keyboard = Calendar.changeMonth(request);
	final Long chatId = request.getChatId();
	drawAnotherMonthCalendar(request, keyboard);
	final LocalDate localDate = Calendar.getDate(request);
	userSessionService.update(SessionUtil.buildSession(chatId, localDate));
	telegramService.sendMessage(chatId, String.format(Messages.DATE, localDate));
  }

  private void drawAnotherMonthCalendar(UserRequest request, InlineKeyboardMarkup keyboard) {
	if(Objects.nonNull(keyboard)) {
	  telegramService.editKeyboardMarkup(request, keyboard);
	  userSessionService.updateState(request.getChatId(), ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
	  throw new RuntimeException("Waiting another date");
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
