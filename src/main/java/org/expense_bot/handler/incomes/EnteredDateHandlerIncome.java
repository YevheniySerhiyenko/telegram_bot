package org.expense_bot.handler.incomes;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.Calendar;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnteredDateHandlerIncome extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isEqual(request, ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
  }

  @Override
  public void handle(UserRequest userRequest) {
    backButtonHandler.handleIncomeBackButton(userRequest);
    final InlineKeyboardMarkup keyboard = Calendar.changeMonth(userRequest);
    final Long chatId = userRequest.getChatId();
    drawAnotherMonthCalendar(userRequest, keyboard);
    LocalDate localDate = getLocalDate(userRequest, keyboard);
    userSessionService.update(SessionUtil.buildIncomeSession(chatId, localDate));
    telegramService.sendMessage(chatId, String.format(Messages.DATE, localDate));
  }

  private LocalDate getLocalDate(UserRequest userRequest, InlineKeyboardMarkup keyboard) {
    return Objects.isNull(keyboard) ? Calendar.getDate(userRequest) : null;
  }

  private void drawAnotherMonthCalendar(UserRequest userRequest, InlineKeyboardMarkup keyboard) {
    if(Objects.nonNull(keyboard)) {
      telegramService.editKeyboardMarkup(userRequest, keyboard);
      userSessionService.updateState(userRequest.getChatId(), ConversationState.Incomes.WAITING_FOR_ANOTHER_INCOME_DATE);
      throw new RuntimeException("Waiting another date");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
