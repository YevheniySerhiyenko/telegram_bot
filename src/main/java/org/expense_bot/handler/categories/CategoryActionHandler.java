package org.expense_bot.handler.categories;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.categories.action_state.CategoryActionState;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Category;
import org.expense_bot.model.Request;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.SessionUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.Objects;
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
  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
	return isStateEqual(request,ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

  @Override
  public void handle(Request request) {
	final Long userId = request.getUserId();
	final CategoryAction categoryAction = CategoryAction.parseAction(getUpdateData(request));
	context.getBean(categoryAction.getHandler()).handle(userId);
	sessionService.update(SessionUtil.getSession(userId, categoryAction));
  }

  @Override
  public boolean isGlobal() {
	return false;
  }
}
