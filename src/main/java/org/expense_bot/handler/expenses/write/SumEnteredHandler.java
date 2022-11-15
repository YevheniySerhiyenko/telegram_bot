package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SumEnteredHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final StickerSender stickerSender;
  private final BackButtonHandler backButtonHandler;


  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM);
  }

  @Override
  public void handle(Request request) {
	sessionService.checkEnteredDate(request, ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE, this.getClass());
	backButtonHandler.handleExpensesBackButton(request);
	final ReplyKeyboard replyKeyboardMarkup = keyboardBuilder.buildExpenseMenu();
	if(hasMessage(request)) {
	  final BigDecimal sum = new BigDecimal(getUpdateData(request));
	  final Long userId = request.getUserId();
	  sessionService.update(SessionUtil.getSession(sum, userId));
	  expenseService.save(ExpenseUtil.getExpense(sessionService.getSession(userId)));
	  telegramService.sendMessage(userId, Messages.SUCCESS);
	  getSticker(userId);
	  telegramService.sendMessage(userId, Messages.SUCCESS_SENT_SUM, replyKeyboardMarkup);
	}
  }

  private void getSticker(Long userId) {
	final String token = stickerSender.getSticker(userId, Messages.SUCCESS_SENT_SUM);
	if(token != null && !token.isEmpty()){
	  telegramService.sendSticker(userId,token);
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
