package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init_handler.BackButtonHandler;
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
public class CategoryActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final CategoryService categoryService;
  private final UserCategoryService userCategoryService;
  private final KeyboardHelper keyboardHelper;
  private final BackButtonHandler backButtonHandler;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate())
	  && ConversationState.WAITING_CATEGORY_ACTION.equals(request.getUserSession().getState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	backButtonHandler.handleBackButton(userRequest);
	final CategoryAction categoryAction = parseAction(userRequest.getUpdate().getMessage().getText());

	if(categoryAction != null) {
	  processAction(userRequest, categoryAction);
	}

	final UserSession session = userRequest.getUserSession();
	session.setState(ConversationState.WAITING_FINAL_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  private CategoryAction parseAction(String text) {
	CategoryAction categoryState = null;
	switch (text) {
	  case Messages.ADD_CATEGORY:
		categoryState = CategoryAction.ADD_NEW_CATEGORY;
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
	session.setState(ConversationState.WAITING_FINAL_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  private void handleAddNew(Long chatId) {
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildBackButtonMenu();
	telegramService.sendMessage(chatId, Messages.ENTER_CATEGORY_NAME,replyKeyboardMarkup);
  }


  private void handleShowAll(Long chatId) {
	final List<String> allCategories = userCategoryService.getByUserId(chatId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(allCategories);
	telegramService.sendMessage(chatId, Messages.ASK_TO_DELETE + " " + Constants.BUTTON_DELETE, replyKeyboardMarkup);
  }

  private void handleAddFromDefault(Long chatId) {
	final List<String> allCategories = categoryService.getDefault().stream().map(Category::getName)
	  .collect(Collectors.toList());
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCustomCategoriesMenu(allCategories);
	telegramService.sendMessage(chatId, Messages.CHOOSE_CATEGORY_FROM_LIST, replyKeyboardMarkup);
  }

}
