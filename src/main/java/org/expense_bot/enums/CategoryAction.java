package org.expense_bot.enums;

import org.expense_bot.constant.Messages;

public enum CategoryAction {

  ADD_NEW_CATEGORY(Messages.ADD_CATEGORY),
  DELETE_MY_CATEGORIES(Messages.DELETE_MY_CATEGORIES),
  ADD_FROM_DEFAULT(Messages.ADD_FROM_DEFAULT);

  private final String value;

  CategoryAction(String value) {
	this.value = value;
  }

  public String getValue() {
	return value;
  }

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
