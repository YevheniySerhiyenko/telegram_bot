package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.User;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryActionRequestHandler extends UserRequestHandler {
  
  private static final String CATEGORY_ACTION = "/categories";

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate(), CATEGORY_ACTION);
  }

  @Override
  public void handle(UserRequest userRequest) {
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCategoryOptionsMenu();
	this.telegramService.sendMessage(userRequest.getChatId(), Messages.CHOOSE_ACTION,replyKeyboardMarkup);
	final Long chatId = userRequest.getChatId();
	final UserSession session = userRequest.getUserSession();
	session.setState(ConversationState.WAITING_CATEGORY_ACTION);
	userSessionService.saveSession(chatId, session);
  }

  @Override
  public boolean isGlobal() {
	return true;
  }

}
