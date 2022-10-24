package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.CategoryState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final CategoryService categoryService;
  private final UserCategoryService userCategoryService;
  private final KeyboardHelper keyboardHelper;
  private final UserService userService;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate()) && CategoryState.WAITING_CATEGORY_ACTION.equals(request.getUserSession().getCategoryState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final CategoryAction categoryAction = parseAction(userRequest.getUpdate().getMessage().getText());

	if(categoryAction != null) {
	  processAction(userRequest, categoryAction);
	}

	final UserSession session = userRequest.getUserSession();
	session.setCategoryState(CategoryState.WAITING_FINAL_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  private CategoryAction parseAction(String text) {
	CategoryAction categoryState = null;
	switch (text) {
	  case Messages.ADD_CATEGORY:
		categoryState = CategoryAction.ADD_NEW_CATEGORY;
		break;
	  case Messages.DELETE_CATEGORY:
		categoryState = CategoryAction.DELETE_CATEGORY;
		break;
	  case Messages.SHOW_MY_CATEGORIES:
		categoryState = CategoryAction.SHOW_MY_CATEGORIES;
		break;
	  case Messages.ADD_FROM_DEFAULT:
		categoryState = CategoryAction.ADD_FROM_DEFAULT;
		break;
	}
	return categoryState;
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

  public void processAction(UserRequest userRequest, CategoryAction categoryAction) {
	final Long chatId = userRequest.getChatId();

	switch (categoryAction) {
	  case DELETE_CATEGORY:
		handleDelete(chatId);
		break;
	  case ADD_NEW_CATEGORY:
		handleAddNew(chatId);
		break;
	  case SHOW_MY_CATEGORIES:
		handleShowAll(chatId);
		break;
	  case ADD_FROM_DEFAULT:
		handleAddFromDefault(chatId);
		break;
	}
	final UserSession session = userRequest.getUserSession();
	session.setCategoryState(CategoryState.WAITING_FINAL_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  private void handleAddNew(Long chatId) {
	telegramService.sendMessage(chatId, Messages.ENTER_CATEGORY_NAME);
  }

  private void handleDelete(Long chatId) {
	final User user = userService.getByChatId(chatId).orElse(null);
	final List<String> allCategories = userCategoryService.getByUser(user)
	  .stream()
	  .map(UserCategory::getCategory)
	  .map(Category::getName)
	  .collect(Collectors.toList());
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(allCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FOR_DELETE, replyKeyboardMarkup);

  }

  private void handleShowAll(Long chatId) {
	final User user = userService.getByChatId(chatId).orElse(null);
	final List<String> allCategories = userCategoryService.getByUser(user)
	  .stream()
	  .map(UserCategory::getCategory)
	  .map(Category::getName)
	  .collect(Collectors.toList());
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(allCategories);
	telegramService.sendMessage(chatId, Messages.YOUR_CATEGORIES, replyKeyboardMarkup);
  }

  private void handleAddFromDefault(Long chatId) {
	final List<String> allCategories = categoryService.getDefault().stream().map(Category::getName)
	  .collect(Collectors.toList());
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(allCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FROM_LIST, replyKeyboardMarkup);
  }

}
