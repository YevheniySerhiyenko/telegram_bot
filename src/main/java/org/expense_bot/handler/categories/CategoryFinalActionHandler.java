package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.CategoryState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CategoryFinalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final CategoryService categoryService;
  private final UserCategoryService userCategoryService;
  private final KeyboardHelper keyboardHelper;

  @Override
  public boolean isApplicable(UserRequest request) {
	return isTextMessage(request.getUpdate()) && CategoryState.WAITING_FINAL_ACTION.equals(request.getUserSession().getCategoryState());
  }

  @Override
  public void handle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	final String categoryParam = userRequest.getUpdate().getMessage().getText();
	final CategoryAction categoryAction = userSessionService.getLastSession(chatId).getCategoryAction();

	switch (categoryAction){
	  case ADD_NEW_CATEGORY:
	  case ADD_FROM_DEFAULT:
		userCategoryService.add(chatId,categoryParam);
		break;
	  case SHOW_MY_CATEGORIES:
	    userCategoryService.delete(chatId,categoryParam);
	    break;

	}
	final UserSession session = userRequest.getUserSession();
	session.setCategoryState(CategoryState.WAITING_CATEGORY_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
	final ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCategoriesMenu(chatId);
	telegramService.sendMessage(chatId, Messages.YOUR_CATEGORIES,replyKeyboardMarkup);
  }




  @Override
  public boolean isGlobal() {
	return false;
  }

}
