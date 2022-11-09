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
import org.expense_bot.util.UserSessionUtil;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;
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
  private final UserSessionUtil userSessionUtil;


  @Override
  public boolean isApplicable(UserRequest userRequest) {
	return ConversationState.Expenses.WAITING_FOR_SUM.equals(userRequest.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	userSessionUtil.checkEnteredDate(userRequest,ConversationState.Expenses.WAITING_FOR_ANOTHER_DATE);
    backButtonHandler.handleExpensesBackButton(userRequest);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildExpenseMenu();
	if(userRequest.getUpdate().hasMessage()) {
	  final BigDecimal sum = new BigDecimal(Utils.getUpdateData(userRequest));
	  final Long chatId = userRequest.getChatId();
	  userSessionService.updateSession(getSession(sum, chatId));
	  expenseService.save(getSpent(userRequest));
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  final String successSentSum = Messages.SUCCESS_SENT_SUM;
	  getSticker(chatId);
	  telegramService.sendMessage(chatId, successSentSum, replyKeyboardMarkup);
	}
  }

  private UserSession getSession(BigDecimal sum, Long chatId) {
	return UserSession.builder().chatId(chatId).expenseSum(sum).state(ConversationState.Init.WAITING_EXPENSE_ACTION).build();
  }

  private void getSticker(Long chatId) {
	final String token = stickerSender.getSticker(chatId, Messages.SUCCESS_SENT_SUM);
	if(token != null && !token.isEmpty()){
	  telegramService.sendSticker(chatId,token);
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  private static Expense getSpent(UserRequest request) {
	final UserSession session = request.getUserSession();
	return Expense.builder()
	  .category(session.getCategory())
	  .sum(session.getExpenseSum())
	  .chatId(session.getChatId())
	  .dateTime(session.getExpenseDate() != null ? session.getExpenseDate().atStartOfDay() : NOW)
	  .build();
  }

}
