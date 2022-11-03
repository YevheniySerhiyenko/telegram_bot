package org.expense_bot.handler.expenses.write;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EnteredDateHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final StickerSender stickerSender;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest userRequest) {
	return ConversationState.WAITING_FOR_SUM_ANOTHER_DATE.equals(userRequest.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboard replyKeyboard = Calendar.handleAnotherDate(userRequest);
	final Long chatId = TelegramUtils.getEffectiveUser(userRequest.getUpdate()).getId();
	if(replyKeyboard != null){
	  telegramService.sendMessage(chatId, "Choose another date", replyKeyboard);
	}
	LocalDate localDate = null;
	if(replyKeyboard == null){
	  localDate = Calendar.getDate(userRequest);
	}
	final UserSession session = userRequest.getUserSession();
	session.setExpenseDate(localDate);
	session.setState(ConversationState.WAITING_FOR_SUM);
	userSessionService.saveSession(chatId, session);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
