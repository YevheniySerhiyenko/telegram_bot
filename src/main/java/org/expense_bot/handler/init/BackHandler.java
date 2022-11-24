package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.Button;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Objects;

import static org.expense_bot.enums.ConversationState.Categories.WAITING_CATEGORY_ACTION;
import static org.expense_bot.enums.ConversationState.Init.WAITING_EXPENSE_ACTION;
import static org.expense_bot.enums.ConversationState.Init.WAITING_INCOME_ACTION;
import static org.expense_bot.enums.ConversationState.Init.WAITING_INIT_ACTION;


@Component
@RequiredArgsConstructor
public class BackHandler {

  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final TelegramService telegramService;

  public boolean handleCategoriesBackButton(Request request) {
	return handleBack(request, keyboardBuilder.buildCategoryOptionsMenu(), WAITING_CATEGORY_ACTION);
  }

  public boolean handleExpensesBackButton(Request request) {
	return handleBack(request, keyboardBuilder.buildExpenseMenu(), WAITING_EXPENSE_ACTION);
  }

  public boolean handleMainMenuBackButton(Request request) {
	return handleBack(request, keyboardBuilder.buildMainMenu(), WAITING_INIT_ACTION);
  }

  public boolean handleIncomeBackButton(Request request) {
	return handleBack(request, keyboardBuilder.buildIncomeMenu(), WAITING_INCOME_ACTION);
  }

  public boolean handleBack(Request request, ReplyKeyboard keyboard, ConversationState state) {
	if(isBack(request)) {
	  final Long userId = request.getUserId();
	  sessionService.updateState(userId, state);
	  telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboard);
	  return true;
	}
	return false;
  }

  private boolean isBack(Request request) {
	if(!RequestHandler.hasMessage(request)) {
	  return false;
	}
	return Objects.equals(RequestHandler.getUpdateData(request), Button.BUTTON_BACK.getValue());
  }

}
