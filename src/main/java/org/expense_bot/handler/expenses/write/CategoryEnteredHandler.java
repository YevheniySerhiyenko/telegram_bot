package org.expense_bot.handler.expenses.write;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.SessionUtil;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CategoryEnteredHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Expenses.WAITING_FOR_CATEGORY);
  }

  @Override
  public void handle(UserRequest request) {
	backButtonHandler.handleExpensesBackButton(request);
	final Long chatId = request.getChatId();
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildSetDateMenu();
	telegramService.sendMessage(chatId, Messages.ENTER_SUM, keyboard);
	final String category = getUpdateData(request);
	userSessionService.update(SessionUtil.buildSession(chatId, category));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
