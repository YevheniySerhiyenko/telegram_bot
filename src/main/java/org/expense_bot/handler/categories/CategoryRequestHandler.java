package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Commands;
import org.expense_bot.constant.Messages;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static org.expense_bot.enums.ConversationState.Categories.WAITING_CATEGORY_ACTION;

@Component
@RequiredArgsConstructor
public class CategoryRequestHandler extends RequestHandler {
  
  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isTextMessage(request.getUpdate(), Commands.CATEGORIES);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleCategoriesBackButton(request);
	final ReplyKeyboard keyboard = keyboardBuilder.buildCategoryOptionsMenu();
	telegramService.sendMessage(request.getUserId(), Messages.CHOOSE_ACTION, keyboard);
	sessionService.updateState(request.getUserId(), WAITING_CATEGORY_ACTION);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }
}
