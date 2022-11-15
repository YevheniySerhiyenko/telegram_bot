package org.expense_bot.handler.categories.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
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
	if(defaultCategories.isEmpty()) {
	  telegramService.sendMessage(userId, Messages.NOTHING_TO_DELETE, keyboardBuilder.buildBackButton());
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(userId, Messages.ASK_TO_DELETE, keyboard);
  }

  @Override
  public void handleFinal(Long userId, String param) {
	userCategoryService.delete(userId, param);
	sendListNotDeleted(userId);
  }

  private List<String> getCategories(Long userId) {
	return userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

  private void sendListNotDeleted(Long chatId) {
	final List<String> userCategories = getUserCategories(chatId);
	final boolean checkAll = checkAll(chatId, userCategories, Messages.ALL_CATEGORIES_DELETED);
	if(checkAll){
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(userCategories);
	telegramService.sendMessage(chatId, Messages.ASK_TO_DELETE, keyboard);
  }

  private boolean checkAll(Long chatId, List<String> categories, String message) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(chatId, message, keyboardBuilder.buildBackButton());
	}
	return categories.isEmpty();
  }

  private List<String> getUserCategories(Long chatId) {
	return userCategoryService.getByUserId(chatId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }


}
