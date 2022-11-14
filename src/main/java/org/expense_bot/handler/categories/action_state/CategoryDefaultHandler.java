package org.expense_bot.handler.categories.action_state;

import org.expense_bot.constant.Messages;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryDefaultHandler implements CategoryActionState {

  @Autowired
  private TelegramService telegramService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private UserCategoryService userCategoryService;
  @Autowired
  private KeyboardBuilder keyboardBuilder;

  @Override
  public void handle(Long userId) {
	List<String> defaultCategories = subtractCategories(userId);
	if(defaultCategories.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.ALL_CATEGORIES_ADDED);
	  throw new RuntimeException(Messages.ALL_CATEGORIES_ADDED);
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


}
