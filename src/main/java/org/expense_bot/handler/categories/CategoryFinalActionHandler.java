package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserRequest;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryFinalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final UserCategoryService userCategoryService;
  private final CategoryService categoryService;
  private final KeyboardBuilder keyboardBuilder;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isEqual(request, ConversationState.Categories.WAITING_FINAL_ACTION);
  }

  @Override
  public void handle(UserRequest userRequest) {
	backButtonHandler.handleCategoriesBackButton(userRequest);
	final Long chatId = userRequest.getChatId();
	final String param = getUpdateData(userRequest);
	final CategoryAction categoryAction = userSessionService.getSession(chatId).getCategoryAction();

	Optional.ofNullable(categoryAction)
	  .ifPresent(action -> {
		switch (action) {
		  case ADD_NEW_CATEGORY:
			addCategory(chatId, param);
			break;
		  case ADD_FROM_DEFAULT:
			userCategoryService.add(chatId, param);
			sendListNotSelected(chatId);
			break;
		  case DELETE_MY_CATEGORIES:
			userCategoryService.delete(chatId, param);
			sendListNotDeleted(chatId);
			break;
		}
	  });

	userSessionService.update(SessionUtil.getSession(chatId, categoryAction));
  }

  private void addCategory(Long chatId, String param) {
	userCategoryService.add(chatId, param);
	telegramService.sendMessage(chatId, Messages.CATEGORY_ADDED_TO_YOUR_LIST, keyboardBuilder.buildCategoryOptionsMenu());
	userSessionService.updateState(chatId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
	throw new RuntimeException(Messages.CATEGORY_ADDED_TO_YOUR_LIST);
  }

  private void sendListNotDeleted(Long chatId) {
	final List<String> userCategories = getUserCategories(chatId);
	checkAll(chatId, userCategories, Messages.ALL_CATEGORIES_DELETED);
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
	checkAll(chatId, defaultCategories, Messages.ALL_CATEGORIES_ADDED);
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FROM_LIST, keyboard);
  }

  private void checkAll(Long chatId, List<String> categories, String message) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(chatId, message, keyboardBuilder.buildBackButtonMenu());
	  throw new RuntimeException(message);
	}
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
