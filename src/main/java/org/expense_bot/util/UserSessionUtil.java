package org.expense_bot.util;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserSessionUtil {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;

  public void checkEnteredDate(UserRequest userRequest, ConversationState state) {
	if(userRequest.getUpdate().hasMessage()) {
	  final String text = userRequest.getUpdate().getMessage().getText();
	  if(text.equals(Messages.ENTER_DATE)) {
		final ReplyKeyboard calendar = Calendar.buildCalendar(LocalDate.now());
		telegramService.sendMessage(userRequest.getChatId(), Messages.ENTER_DATE, calendar);
		final UserSession session = userRequest.getUserSession();
		session.setState(state);
		final Long chatId = userRequest.getChatId();
		userSessionService.saveSession(chatId, session);
		throw new RuntimeException("Build calendar");
	  }
	}
	if(userRequest.getUpdate().hasCallbackQuery()) {
	  final UserSession session = userRequest.getUserSession();
	  final Long chatId = userRequest.getChatId();
	  final InlineKeyboardMarkup keyboardMarkup = Calendar.changeMonth(userRequest);
	  if(keyboardMarkup == null) {
		final LocalDate date = Calendar.getDate(userRequest);
		telegramService.editMessage(userRequest, String.format(Messages.DATE, date));
		session.setExpenseDate(date);
		userSessionService.saveSession(chatId, session);
		throw new RuntimeException("Change day");
	  }
	  telegramService.editKeyboardMarkup(userRequest,keyboardMarkup);
	}
  }

}
