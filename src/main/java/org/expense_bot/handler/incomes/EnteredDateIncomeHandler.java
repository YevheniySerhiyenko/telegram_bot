package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.model.Request;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnteredDateIncomeHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackHandler backHandler;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
  }

  @Override
  public void handle(Request request) {
    if(backHandler.handleIncomeBackButton(request)){
      return;
    }
    final InlineKeyboardMarkup keyboard = Calendar.changeMonth(request);
    final Long userId = request.getUserId();
    final boolean anotherMonth = drawAnotherMonthCalendar(request, keyboard);
    if(anotherMonth) {
      return;
    }
    final LocalDate localDate = getLocalDate(request, keyboard);
    sessionService.update(SessionUtil.buildIncomeSession(userId, localDate));
    telegramService.sendMessage(userId, String.format(Messages.DATE, localDate));
  }

  private LocalDate getLocalDate(Request request, InlineKeyboardMarkup keyboard) {
    return Objects.isNull(keyboard) ? Calendar.getDate(request) : null;
  }

  private boolean drawAnotherMonthCalendar(Request request, InlineKeyboardMarkup keyboard) {
    if(Objects.isNull(keyboard)) {
      return false;
    }
    telegramService.editKeyboardMarkup(request, keyboard);
    sessionService.updateState(request.getUserId(), ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
    return true;
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}