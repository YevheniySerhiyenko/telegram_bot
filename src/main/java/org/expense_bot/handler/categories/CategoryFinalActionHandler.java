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
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.UserSessionService;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
	final Category category = getCategory(categoryParam);

	switch (categoryAction){
	  case ADD_NEW_CATEGORY:
	  case ADD_FROM_DEFAULT:
		userCategoryService.add(chatId,category);
		break;
	  case DELETE_CATEGORY:
	    userCategoryService.delete(chatId,category);
	    break;
	}

	final UserSession session = userRequest.getUserSession();
	session.setCategoryState(CategoryState.WAITING_FINAL_ACTION);
	session.setCategoryAction(categoryAction);
	userSessionService.saveSession(chatId, session);
  }

  private Category getCategory(String categoryParam) {
	final Optional<Category> byName = categoryService.findByName(categoryParam);
	return byName.orElseGet(() -> categoryService.create(buildCategory(categoryParam)));
  }

  private Category buildCategory(String categoryParam) {
	return Category.builder()
	  .name(categoryParam)
	  .build();
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
