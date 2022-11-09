package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EnteredDateHandlerIncome extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return ConversationState.Incomes.WAITING_FOR_ANOTHER_DATE.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
    backButtonHandler.handleIncomeBackButton(userRequest);
    final InlineKeyboardMarkup replyKeyboard = Calendar.changeMonth(userRequest);
    final Long chatId = Utils.getEffectiveUser(userRequest.getUpdate()).getId();
    drawAnotherMonthCalendar(userRequest, replyKeyboard);
    LocalDate localDate = getLocalDate(userRequest, replyKeyboard);
    final UserSession session = userRequest.getUserSession();
    session.setIncomeDate(localDate);
    session.setState(ConversationState.Incomes.WAITING_FOR_SUM);
    userSessionService.saveSession(session);
    telegramService.sendMessage(chatId, String.format(Messages.DATE, localDate));
  }

  private LocalDate getLocalDate(UserRequest userRequest, InlineKeyboardMarkup replyKeyboard) {
    LocalDate localDate = null;
    if(replyKeyboard == null) {
      localDate = Calendar.getDate(userRequest);
    }
    return localDate;
  }

  private void drawAnotherMonthCalendar(UserRequest userRequest, InlineKeyboardMarkup replyKeyboard) {
    if(replyKeyboard != null) {
      telegramService.editKeyboardMarkup(userRequest, replyKeyboard);
      final UserSession session = userRequest.getUserSession();
      session.setState(ConversationState.Incomes.WAITING_FOR_ANOTHER_DATE);
      throw new RuntimeException("Waiting another date");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
