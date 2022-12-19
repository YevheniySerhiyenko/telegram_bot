package expense_bot.handler.expenses.check;

import expense_bot.constant.Messages;
import expense_bot.enums.Button;
import expense_bot.enums.ConversationState;
import expense_bot.enums.Period;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.model.UserCategory;
import expense_bot.service.UserCategoryService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.Calendar;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CheckPeriodHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final BackHandler backHandler;
  private final UserCategoryService userCategoryService;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_PERIOD);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final String period = getUpdateData(request);
    final boolean showCalendar = checkRequest(userId, period);
    if (showCalendar) {
      return;
    }
    final ReplyKeyboard keyboard = KeyboardBuilder.buildCategories(getCategories(userId));
    sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_CHECK_CATEGORY));
    telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY, keyboard);
  }


  private boolean checkRequest(Long userId, String period) {
    if (!Objects.equals(period, Period.PERIOD.getValue())) {
      return false;
    }
    telegramService.sendMessage(userId, Messages.CHOOSE_PERIOD, Calendar.buildCalendar(LocalDate.now()));
    sessionService.update(SessionUtil.getSession(userId, period, ConversationState.Expenses.WAITING_FOR_TWO_DATES));
    return true;
  }

  private List<String> getCategories(Long userId) {
    final List<String> categories = new ArrayList<>();
    categories.add(Button.BY_ALL_CATEGORIES.getValue());
    userCategoryService.getByUserId(userId)
      .stream()
      .map(UserCategory::getCategory)
      .forEach(categories::add);
    return categories;
  }
  @Override
  public boolean isGlobal() {
    return false;
  }

}
