package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.Button;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.expense_bot.handler.RequestHandler.getUpdateData;

@Component
@RequiredArgsConstructor
public class SessionService {

  private final Map<Long, Session> userSessionMap = new HashMap<>();
  private final TelegramService telegramService;
  private final StickerSender stickerSender;

  public Session getSession(Long userId) {
	return userSessionMap.getOrDefault(userId, Session
	  .builder()
	  .userId(userId)
	  .build());
  }

  private void saveSession(Session session) {
	userSessionMap.put(session.getUserId(), session);
  }

  public void updateState(Long userId, ConversationState state) {
	final Session session = getSession(userId);
	session.setState(state);
	saveSession(session);
  }

  public void update(Session userSession) {
	final Session session = getSession(userSession.getUserId());
	Optional.ofNullable(userSession.getState()).ifPresent(session::setState);
	Optional.ofNullable(userSession.getCategoryAction()).ifPresent(session::setCategoryAction);
	Optional.ofNullable(userSession.getAction()).ifPresent(session::setAction);
	Optional.ofNullable(userSession.getCategory()).ifPresent(session::setCategory);
	Optional.ofNullable(userSession.getExpenseSum()).ifPresent(session::setExpenseSum);
	Optional.ofNullable(userSession.getIncomeSum()).ifPresent(session::setIncomeSum);
	Optional.ofNullable(userSession.getExpenseDate()).ifPresent(session::setExpenseDate);
	Optional.ofNullable(userSession.getIncomeDate()).ifPresent(session::setIncomeDate);
	Optional.ofNullable(userSession.getIncomeAction()).ifPresent(session::setIncomeAction);
	Optional.ofNullable(userSession.getPeriod()).ifPresent(session::setPeriod);
	Optional.ofNullable(userSession.getStickerAction()).ifPresent(session::setStickerAction);
	Optional.ofNullable(userSession.getExpenseList()).ifPresent(session::setExpenseList);
	Optional.ofNullable(userSession.getIncomeList()).ifPresent(session::setIncomeList);
	Optional.ofNullable(userSession.getPassword()).ifPresent(session::setPassword);
	Optional.ofNullable(userSession.getPasswordAction()).ifPresent(session::setPasswordAction);
	Optional.of(userSession.isEnablePassword()).ifPresent(session::setEnablePassword);
	Optional.ofNullable(userSession.getPasswordConfirmed()).ifPresent(session::setPasswordConfirmed);

	session.setPeriodFrom(userSession.getPeriodFrom());
	session.setPeriodTo(userSession.getPeriodTo());
	saveSession(session);
  }

  public boolean checkEnteredDate(Request request, ConversationState state) {
	boolean enteredDate = false;
	final Long userId = request.getUserId();
	if(RequestHandler.hasMessage(request)) {
	  final String text = RequestHandler.getUpdateData(request);
	  if(Objects.equals(text, Button.ENTER_DATE.getValue())) {
		final ReplyKeyboard calendar = Calendar.buildCalendar(LocalDate.now());
		telegramService.sendMessage(userId, Button.ENTER_DATE.getValue(), calendar);
		updateState(userId, state);
		enteredDate = true;
	  }
	}
	if(RequestHandler.hasCallBack(request)) {
	  final Optional<InlineKeyboardMarkup> keyboard = Calendar.changeMonth(request);
	  if(keyboard.isEmpty()) {
		final LocalDate date = Calendar.getDate(request);
		telegramService.editNextMessage(request, String.format(Messages.DATE, date));
		updateSession(userId, date, state);
	  } else {
		telegramService.editKeyboardMarkup(request, keyboard.get());
		enteredDate = true;
	  }
	}
	return enteredDate;
  }

  public Optional<BigDecimal> getSum(Request request) {
	try {
	  return Optional.of(new BigDecimal(getUpdateData(request)));
	} catch (NumberFormatException e) {
	  final Long userId = request.getUserId();
	  stickerSender.sendSticker(userId, StickerAction.WRONG_ENTERED_SUM.name());
	  telegramService.sendMessage(userId, Messages.WRONG_SUM_FORMAT);
	}
	return Optional.empty();
  }

  private void updateSession(Long userId, LocalDate date, ConversationState state) {
	if(state instanceof ConversationState.Expenses) {
	  update(SessionUtil.buildExpenseSession(userId, date));
	} else {
	  update(SessionUtil.buildIncomeSession(userId, date));
	}
  }


}
