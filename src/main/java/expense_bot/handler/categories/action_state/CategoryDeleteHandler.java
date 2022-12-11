package expense_bot.handler.categories.action_state;

import expense_bot.constant.Messages;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.UserCategory;
import expense_bot.service.UserCategoryService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryDeleteHandler implements CategoryActionState {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserCategoryService userCategoryService;

  @Override
  public void handle(Long userId) {
    final List<String> defaultCategories = getCategories(userId);
    if (defaultCategories.isEmpty()) {
      telegramService.sendMessage(userId, Messages.NOTHING_TO_DELETE, keyboardBuilder.buildBackButton());
      return;
    }
    final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
    telegramService.sendMessage(userId, Messages.ASK_TO_DELETE, keyboard);
  }

  @Override
  public void handleFinal(Long userId, String categoryParam) {
    userCategoryService.delete(userId, categoryParam);
    sendListNotDeleted(userId);
  }

  private List<String> getCategories(Long userId) {
    return userCategoryService.getByUserId(userId)
      .stream()
      .map(UserCategory::getCategory)
      .collect(Collectors.toList());
  }

  private void sendListNotDeleted(Long userId) {
    final List<String> userCategories = getUserCategories(userId);
    final boolean allDeleted = checkAll(userId, userCategories);
    if (allDeleted) {
      return;
    }
    final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(userCategories);
    telegramService.sendMessage(userId, Messages.ASK_TO_DELETE, keyboard);
  }

  private boolean checkAll(Long userId, List<String> categories) {
    if (categories.isEmpty()) {
      final ReplyKeyboard backButton = keyboardBuilder.buildBackButton();
      telegramService.sendMessage(userId, Messages.ALL_CATEGORIES_DELETED, backButton);
    }
    return categories.isEmpty();
  }

  private List<String> getUserCategories(Long userId) {
    return userCategoryService.getByUserId(userId)
      .stream()
      .map(UserCategory::getCategory)
      .collect(Collectors.toList());
  }


}
