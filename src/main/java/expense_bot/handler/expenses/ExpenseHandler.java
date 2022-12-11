package expense_bot.handler.expenses;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.enums.ExpenseAction;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.model.UserCategory;
import expense_bot.service.UserCategoryService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserCategoryService userCategoryService;
  private final BackHandler backHandler;
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Init.WAITING_EXPENSE_ACTION);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleMainMenuBackButton(request)) {
      return;
    }
    final Long userId = request.getUserId();
    final List<UserCategory> categories = userCategoryService.getByUserId(userId);
    final ExpenseAction action = ExpenseAction.parse(getUpdateData(request));
    if (checkEmpty(userId, categories)) {
      return;
    }
    context.getBean(action.getHandler()).handle(userId);
  }

  private boolean checkEmpty(Long userId, List<UserCategory> categories) {
    if (categories.isEmpty()) {
      telegramService.sendMessage(userId, Messages.NO_ONE_CATEGORY_FOUND, keyboardBuilder.buildBackButton());
    }
    return categories.isEmpty();
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
