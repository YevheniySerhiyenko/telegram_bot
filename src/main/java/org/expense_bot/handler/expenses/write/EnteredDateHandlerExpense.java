package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EnteredDateHandlerExpense extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final StickerSender stickerSender;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest userRequest) {
	return ConversationState.Expenses.WAITING_FOR_ANOTHER_DATE.equals(userRequest.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleExpensesBackButton(userRequest);
	final InlineKeyboardMarkup replyKeyboard = Calendar.changeMonth(userRequest);
	final Long chatId = Utils.getEffectiveUser(userRequest.getUpdate()).getId();
	drawAnotherMonthCalendar(userRequest, replyKeyboard);
	LocalDate localDate = null;
	if(replyKeyboard == null){
	  localDate = Calendar.getDate(userRequest);
	}
	userSessionService.updateSession(getSession(chatId, localDate));
	telegramService.sendMessage(chatId, String.format(Messages.DATE,localDate));
  }

  private UserSession getSession(Long chatId, LocalDate localDate) {
	return UserSession.builder()
	  .chatId(chatId)
	  .expenseDate(localDate)
	  .state(ConversationState.Expenses.WAITING_FOR_SUM)
	  .build();
  }

  private void drawAnotherMonthCalendar(UserRequest userRequest, InlineKeyboardMarkup replyKeyboard) {
	if(replyKeyboard != null){
	  telegramService.editKeyboardMarkup(userRequest, replyKeyboard);
	  final UserSession session = userRequest.getUserSession();
	  session.setState(ConversationState.Expenses.WAITING_FOR_ANOTHER_DATE);
	  throw new RuntimeException("Waiting another date");
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
