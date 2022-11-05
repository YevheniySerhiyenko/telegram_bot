package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
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
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate(), COMMAND);
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleCategoriesBackButton(userRequest);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCategoryOptionsMenu();
	telegramService.sendMessage(userRequest.getChatId(), Messages.CHOOSE_ACTION,replyKeyboardMarkup);
	final Long chatId = userRequest.getChatId();
	final UserSession session = userRequest.getUserSession();
	session.setState(ConversationState.Categories.WAITING_CATEGORY_ACTION);
	userSessionService.saveSession(chatId, session);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }
}
