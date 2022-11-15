package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Category;
import org.expense_bot.model.Request;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryFinalActionHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final UserCategoryService userCategoryService;
  private final CategoryService categoryService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request, ConversationState.Categories.WAITING_FINAL_ACTION);
  }

  @Override
  public void handle(Request request) {
	backButtonHandler.handleCategoriesBackButton(request);
	final Long userId = request.getUserId();
	final String param = getUpdateData(request);
	final CategoryAction categoryAction = sessionService.getSession(userId).getCategoryAction();

	if(categoryAction != null) {
	  switch (categoryAction) {
		case ADD_NEW_CATEGORY:
		  addCategory(userId, param);
		  return;
		case ADD_FROM_DEFAULT:
		  userCategoryService.add(userId, param);
		  sendListNotSelected(userId);
		case DELETE_MY_CATEGORIES:
		  userCategoryService.delete(userId, param);
		  sendListNotDeleted(userId);
		  break;
	  }
	}

	sessionService.update(SessionUtil.getSession(userId, categoryAction));
  }

  private void addCategory(Long chatId, String param) {
	userCategoryService.add(chatId, param);
	telegramService.sendMessage(chatId, Messages.CATEGORY_ADDED_TO_YOUR_LIST, keyboardBuilder.buildCategoryOptionsMenu());
	sessionService.updateState(chatId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
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

  private void sendListNotSelected(Long chatId) {
	final List<String> userCategories = getUserCategories(chatId);
	final List<String> defaultCategories = categoryService.getDefault()
	  .stream()
	  .map(Category::getName)
	  .collect(Collectors.toList());
	defaultCategories.removeAll(userCategories);
	final boolean checkAll = checkAll(chatId, defaultCategories, Messages.ALL_CATEGORIES_ADDED);
	if(checkAll){
	  return;
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FROM_LIST, keyboard);
  }

  private boolean checkAll(Long chatId, List<String> categories, String message) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(chatId, message, keyboardBuilder.buildBackButton());
	}
	return categories.isEmpty();
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  private List<String> getUserCategories(Long chatId) {
	return userCategoryService.getByUserId(chatId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

}
