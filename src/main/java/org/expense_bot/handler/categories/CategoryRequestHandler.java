package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CategoryRequestHandler extends UserRequestHandler {
  
  private static final String COMMAND = "/categories";

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate(), COMMAND);
  }

  @Override
  public void handle(UserRequest request) {
	backButtonHandler.handleCategoriesBackButton(request);
	final ReplyKeyboardMarkup keyboard = keyboardBuilder.buildCategoryOptionsMenu();
	telegramService.sendMessage(request.getChatId(), Messages.CHOOSE_ACTION, keyboard);
	final Long chatId = request.getChatId();
	userSessionService.updateState(chatId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }
}
