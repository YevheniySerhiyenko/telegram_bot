package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.ExpenseUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SumEnteredHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final StickerSender stickerSender;
  private final BackButtonHandler backButtonHandler;


  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM);
  }

  @Override
  public void handle(UserRequest request) {
	userSessionService.checkEnteredDate(request, ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE, this.getClass());
	backButtonHandler.handleExpensesBackButton(request);
	final ReplyKeyboard replyKeyboardMarkup = keyboardBuilder.buildExpenseMenu();
	if(hasMessage(request)) {
	  final BigDecimal sum = new BigDecimal(getUpdateData(request));
	  final Long chatId = request.getChatId();
	  userSessionService.update(SessionUtil.getSession(sum, chatId));
	  expenseService.save(ExpenseUtil.getExpense(userSessionService.getSession(chatId)));
	  telegramService.sendMessage(chatId, Messages.SUCCESS);
	  getSticker(chatId);
	  telegramService.sendMessage(chatId, Messages.SUCCESS_SENT_SUM, replyKeyboardMarkup);
	}
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

}
