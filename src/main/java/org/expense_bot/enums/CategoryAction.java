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
}
