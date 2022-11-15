package org.expense_bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.handler.categories.action_state.CategoryActionState;
import org.expense_bot.handler.categories.action_state.CategoryAddNewHandler;
import org.expense_bot.handler.categories.action_state.CategoryDefaultHandler;
import org.expense_bot.handler.categories.action_state.CategoryDeleteHandler;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CategoryAction {

  ADD_NEW_CATEGORY(Messages.ADD_CATEGORY, CategoryAddNewHandler.class),
  DELETE_MY_CATEGORIES(Messages.DELETE_MY_CATEGORIES, CategoryDeleteHandler.class),
  ADD_FROM_DEFAULT(Messages.ADD_FROM_DEFAULT, CategoryDefaultHandler.class);

  private final String value;
  private final Class<? extends CategoryActionState> handler;

  public static CategoryAction parseAction(String text) {
	switch (text) {
	  case Messages.ADD_CATEGORY:
		return CategoryAction.ADD_NEW_CATEGORY;
	  case Messages.DELETE_MY_CATEGORIES:
		return CategoryAction.DELETE_MY_CATEGORIES;
	  case Messages.ADD_FROM_DEFAULT:
		return CategoryAction.ADD_FROM_DEFAULT;
	  default:
		return null;
	}
  }
}
