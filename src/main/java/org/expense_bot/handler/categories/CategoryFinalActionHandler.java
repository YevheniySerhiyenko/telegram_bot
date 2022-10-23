package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.CategoryState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Category;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.TelegramService;
import org.expense_bot.service.UserSessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFinalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final CategoryService categoryService;
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
		categoryService.addToUser(chatId,categoryParam);
		break;
	  case DELETE_CATEGORY:
	    categoryService.deleteFromUser(chatId,categoryParam);
	    break;
	  case ADD_FROM_DEFAULT:
		categoryService.addToUser(chatId,categoryParam);
		break;
	}

	final UserSession session = userRequest.getUserSession();
	session.setCategoryState(CategoryState.PROCESS_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
