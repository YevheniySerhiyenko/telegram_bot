package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnteredDateHandlerIncome extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
  }

  @Override
  public void handle(Request userRequest) {
    backButtonHandler.handleIncomeBackButton(userRequest);
    final InlineKeyboardMarkup keyboard = Calendar.changeMonth(userRequest);
    final Long chatId = userRequest.getUserId();
    drawAnotherMonthCalendar(userRequest, keyboard);
    LocalDate localDate = getLocalDate(userRequest, keyboard);
    sessionService.update(SessionUtil.buildIncomeSession(chatId, localDate));
    telegramService.sendMessage(chatId, String.format(Messages.DATE, localDate));
  }

  private LocalDate getLocalDate(Request userRequest, InlineKeyboardMarkup keyboard) {
    return Objects.isNull(keyboard) ? Calendar.getDate(userRequest) : null;
  }

  private void drawAnotherMonthCalendar(Request userRequest, InlineKeyboardMarkup keyboard) {
    if(Objects.nonNull(keyboard)) {
      telegramService.editKeyboardMarkup(userRequest, keyboard);
      sessionService.updateState(userRequest.getUserId(), ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
      throw new RuntimeException("Waiting another date");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
