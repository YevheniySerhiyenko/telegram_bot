package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryActionHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final CategoryService categoryService;
  private final UserCategoryService userCategoryService;
  private final KeyboardBuilder keyboardBuilder;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request,ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

  @Override
  public void handle(Request userRequest) {
	final Long chatId = userRequest.getUserId();
	final CategoryAction categoryAction = CategoryAction.parseAction(getUpdateData(userRequest));

	Optional.ofNullable(categoryAction)
	  .ifPresent(action -> {
		switch (action) {
		  case ADD_NEW_CATEGORY:
			handleAddNew(chatId);
			break;
		  case DELETE_MY_CATEGORIES:
			handleDelete(chatId);
			break;
		  case ADD_FROM_DEFAULT:
			handleAddFromDefault(chatId);
			break;
		}
	  });

	sessionService.update(SessionUtil.getSession(chatId, categoryAction));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  private void handleAddNew(Long chatId) {
	telegramService.sendMessage(chatId, Messages.ENTER_CATEGORY_NAME, keyboardBuilder.buildBackButton());
  }

  private void handleDelete(Long chatId) {
	final List<String> defaultCategories = getCategories(chatId);
	if(defaultCategories.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.NOTHING_TO_DELETE, keyboardBuilder.buildBackButton());
	  throw new RuntimeException(Messages.NOTHING_TO_DELETE);
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(chatId, Messages.ASK_TO_DELETE, keyboard);
  }

  private void handleAddFromDefault(Long chatId) {
	List<String> defaultCategories = subtractCategories(chatId);
	if(defaultCategories.isEmpty()) {
	  telegramService.sendMessage(chatId, Messages.ALL_CATEGORIES_ADDED);
	  throw new RuntimeException(Messages.ALL_CATEGORIES_ADDED);
	}
	final ReplyKeyboard keyboard = keyboardBuilder.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FROM_LIST, keyboard);
  }

  private List<String> getCategories(Long chatId) {
	return userCategoryService.getByUserId(chatId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

  public List<String> subtractCategories(Long chatId) {
	final List<String> defaultCategories = categoryService.getDefault()
	  .stream()
	  .map(Category::getName)
	  .collect(Collectors.toList());
	final List<String> userCategories = getCategories(chatId);
	defaultCategories.removeAll(userCategories);
	return defaultCategories;
  }


}
