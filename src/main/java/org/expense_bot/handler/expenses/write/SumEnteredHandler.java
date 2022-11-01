package org.expense_bot.handler.expenses.write;

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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SumEnteredHandler extends UserRequestHandler {

  private static final LocalDateTime NOW = LocalDateTime.now();

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final StickerSender stickerSender;
  private final BackButtonHandler backButtonHandler;


  @Override
  public boolean isApplicable(UserRequest userRequest) {
	return isTextMessage(userRequest.getUpdate()) && ConversationState.WAITING_FOR_SUM.equals(userRequest.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final String text = userRequest.getUpdate().getMessage().getText();
	if(text.equals(Messages.ENTER_DATE)){
	  final ReplyKeyboard calendar = Calendar.buildCalendar(LocalDate.now().getMonth());
	  telegramService.sendMessage(userRequest.getChatId(),Messages.ENTER_DATE, calendar);
	}
    backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildExpenseMenu();
	final BigDecimal sum = new BigDecimal(userRequest.getUpdate().getMessage().getText());
	final UserSession session = userRequest.getUserSession();
	session.setExpenseSum(sum);
	session.setState(ConversationState.CONVERSATION_STARTED);
	final Long chatId = userRequest.getChatId();
	userSessionService.saveSession(chatId, session);
	expenseService.save(getSpent(session));
	telegramService.sendMessage(chatId, Messages.SUCCESS);
	final String successSentSum = Messages.SUCCESS_SENT_SUM;
	getSticker(chatId, successSentSum);
	telegramService.sendMessage(chatId, successSentSum, replyKeyboardMarkup);
  }

  private void getSticker(Long chatId, String successSentSum) {
	final String token = stickerSender.getSticker(chatId, successSentSum);
	if(token != null && !token.isEmpty()){
	  telegramService.sendSticker(chatId,token);
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  private static Expense getSpent(UserSession session) {
	return Expense.builder()
	  .category(session.getCategory())
	  .sum(session.getExpenseSum())
	  .chatId(session.getChatId())
	  .dateTime(NOW)
	  .build();
  }

}
