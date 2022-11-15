package org.expense_bot.handler.categories.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryDefaultHandler implements CategoryActionState {

  private final TelegramService telegramService;
  private final CategoryService categoryService;
  private final UserCategoryService userCategoryService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
	List<String> defaultCategories = subtractCategories(userId);
	if(defaultCategories.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.ALL_CATEGORIES_ADDED);
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY_FROM_LIST, keyboard);
  }

  @Override
  public void handleFinal(Long userId, String categoryParam) {
	userCategoryService.add(userId, categoryParam);
	sendListNotSelected(userId);
  }

  private void sendListNotSelected(Long userId) {
	final List<String> userCategories = getUserCategories(userId);
	final List<String> defaultCategories = categoryService.getDefault()
	  .stream()
	  .map(Category::getName)
	  .collect(Collectors.toList());
	defaultCategories.removeAll(userCategories);
	if(checkAll(userId, defaultCategories)){
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(userId, Messages.CHOOSE_CATEGORY_FROM_LIST, keyboard);
  }

  public List<String> subtractCategories(Long userId) {
	final List<String> defaultCategories = categoryService.getDefault()
	  .stream()
	  .map(Category::getName)
	  .collect(Collectors.toList());
	final List<String> userCategories = getCategories(userId);
	defaultCategories.removeAll(userCategories);
	return defaultCategories;
  }

  private List<String> getCategories(Long userId) {
	return userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

  private boolean checkAll(Long userId, List<String> categories) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.ALL_CATEGORIES_ADDED, keyboardBuilder.buildBackButton());
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
