package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class CategoryEnteredHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_CATEGORY);
  }

  @Override
  public void handle(Request request) {
	if(backHandler.handleExpensesBackButton(request)) {
	  return;
	}
	final Long userId = request.getUserId();
	final ReplyKeyboard keyboard = keyboardBuilder.buildSetDateMenu();
	telegramService.sendMessage(userId, Messages.ENTER_SUM, keyboard);
	final String category = getUpdateData(request);
	sessionService.update(SessionUtil.buildSession(userId, category));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
