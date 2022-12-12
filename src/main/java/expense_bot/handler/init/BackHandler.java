package expense_bot.handler.init;

import expense_bot.constant.Messages;
import expense_bot.enums.Button;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class BackHandler {

  private final SessionService sessionService;
  private final TelegramService telegramService;

  public boolean handleCategoriesBackButton(Request request) {
    final ReplyKeyboard keyboard = KeyboardBuilder.buildCategoryOptionsMenu();
    return handleBack(request, keyboard, ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

  public boolean handleExpensesBackButton(Request request) {
    return handleBack(request, KeyboardBuilder.buildExpenseMenu(), ConversationState.Init.WAITING_EXPENSE_ACTION);
  }

  public boolean handleBackPasswordSetting(Request request) {
    final ReplyKeyboard keyboard = KeyboardBuilder.buildSettingsMenu();
    return handleBack(request, keyboard, ConversationState.Settings.WAITING_SETTINGS_ACTION);
  }

  public boolean handleMainMenuBackButton(Request request) {
    return handleBack(request, KeyboardBuilder.buildMainMenu(), ConversationState.Init.WAITING_INIT_ACTION);
  }

  public boolean handleIncomeBackButton(Request request) {
    return handleBack(request, KeyboardBuilder.buildIncomeMenu(), ConversationState.Init.WAITING_INCOME_ACTION);
  }

  private boolean handleBack(Request request, ReplyKeyboard keyboard, ConversationState state) {
    if (isBack(request)) {
      final Long userId = request.getUserId();
      sessionService.updateState(userId, state);
      telegramService.sendMessage(userId, Messages.CHOOSE_ACTION, keyboard);
      return true;
    }
    return false;
  }

  private boolean isBack(Request request) {
    if (!RequestHandler.hasMessage(request)) {
      return false;
    }
    return Objects.equals(RequestHandler.getUpdateData(request), Button.BUTTON_BACK.getValue());
  }

}
