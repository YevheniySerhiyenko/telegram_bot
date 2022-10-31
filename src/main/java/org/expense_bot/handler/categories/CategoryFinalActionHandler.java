package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryFinalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final UserCategoryService userCategoryService;
  private final CategoryService categoryService;
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_FINAL_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final String param = userRequest.getUpdate().getMessage().getText();
	backButtonHandler.handleCategoriesBackButton(userRequest);
	final CategoryAction categoryAction = userSessionService.getLastSession(chatId).getCategoryAction();

	switch (categoryAction) {
	  case ADD_NEW_CATEGORY:
	  case ADD_FROM_DEFAULT:
		userCategoryService.add(chatId, param);
		sendListNotSelected(chatId);
		break;
	  case DELETE_MY_CATEGORIES:
		userCategoryService.delete(chatId, param);
		sendListNotDeleted(chatId);
		break;

	}
	final UserSession session = userRequest.getUserSession();
	session.setState(ConversationState.WAITING_FINAL_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  private void sendListNotDeleted(Long chatId) {
	final List<String> userCategories = userCategoryService.getByUserId(chatId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
	checkAll(chatId, userCategories, Messages.ALL_CATEGORIES_DELETED);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(userCategories);
	telegramService.sendMessage(chatId, Messages.ASK_TO_DELETE, replyKeyboardMarkup);
  }

  private void sendListNotSelected(Long chatId) {
	final List<String> userCategories = userCategoryService.getByUserId(chatId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
	final List<String> defaultCategories = categoryService.getDefault()
	  .stream()
	  .map(Category::getName)
	  .collect(Collectors.toList());
	defaultCategories.removeAll(userCategories);
	checkAll(chatId, defaultCategories, Messages.ALL_CATEGORIES_ADDED);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(defaultCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FROM_LIST, replyKeyboardMarkup);
  }

  private void checkAll(Long chatId, List<String> categories, String message) {
	if(categories.isEmpty()) {
	  telegramService.sendMessage(chatId, message, keyboardHelper.buildBackButtonMenu());
	  throw new RuntimeException(message);
	}
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
