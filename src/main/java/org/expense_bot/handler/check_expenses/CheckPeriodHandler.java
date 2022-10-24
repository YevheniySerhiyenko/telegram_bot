package org.expense_bot.handler.check_expenses;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_FOR_PERIOD.equals(request.getUserSession().getState());

  }

  @Override
  public void handle(UserRequest userRequest) {
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCheckCategoriesMenu();
	final Long chatId = userRequest.getChatId();
	telegramService.sendMessage(chatId, "Оберіть категорію!", replyKeyboardMarkup);
	final String period = userRequest.getUpdate().getMessage().getText();
	final UserSession session = userRequest.getUserSession();
	session.setPeriod(period);
	session.setState(ConversationState.WAITING_CHECK_CATEGORY);
	userSessionService.saveSession(chatId, session);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
