package org.expense_bot.handler.init;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class BackButtonHandler {

  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final TelegramService telegramService;

  public void handleCategoriesBackButton(Request request) {
	if(isBack(request)) {
	  final Long chatId = request.getUserId();
	  sessionService.updateState(chatId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildCategoryOptionsMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	  throw new RuntimeException("Handle back button in categoris");
	}
  }

  public void handleExpensesBackButton(Request request) {
	if(isBack(request)) {
	  final Long chatId = request.getUserId();
	  sessionService.updateState(chatId, ConversationState.Init.WAITING_EXPENSE_ACTION);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildExpenseMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	  throw new RuntimeException("Handle expenses back button");
	}
  }

  public void handleMainMenuBackButton(Request request) {
	if(isBack(request)) {
	  final Long chatId = request.getUserId();
	  sessionService.updateState(chatId, ConversationState.Init.WAITING_INIT_ACTION);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildMainMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	  throw new RuntimeException("Handle main menu back button");
	}
  }

  public void handleIncomeBackButton(Request request) {
	if(isBack(request)) {
	  final Long chatId = request.getUserId();
	  sessionService.updateState(chatId, ConversationState.Init.WAITING_INCOME_ACTION);
	  final ReplyKeyboard keyboard = keyboardBuilder.buildIncomeMenu();
	  telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION, keyboard);
	  throw new RuntimeException("Handle incomes back button");
	}
  }

  private boolean isBack(Request request) {
	if(!RequestHandler.hasMessage(request)) {
	  return false;
	}
	return Objects.equals(RequestHandler.getUpdateData(request), Buttons.BUTTON_BACK);
  }

}
