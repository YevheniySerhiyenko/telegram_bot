package org.expense_bot.handler.categories.action_state;

import org.expense_bot.constant.Messages;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryDeleteHandler implements CategoryActionState {

  @Autowired
  private TelegramService telegramService;
  @Autowired
  private KeyboardBuilder keyboardBuilder;
  @Autowired
  private UserCategoryService userCategoryService;

  @Override
  public void handle(Long userId) {
	final List<String> defaultCategories = getCategories(userId);
	if(defaultCategories.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.NOTHING_TO_DELETE, keyboardBuilder.buildBackButton());
	  throw new RuntimeException(Messages.NOTHING_TO_DELETE);
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(userId, Messages.ASK_TO_DELETE, keyboard);
  }

  private List<String> getCategories(Long userId) {
	return userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

}
