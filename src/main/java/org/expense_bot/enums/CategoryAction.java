package org.expense_bot.enums;

public enum CategoryAction {

  ADD_NEW_CATEGORY("Додати свою категорію"),
  DELETE_MY_CATEGORIES("Видалити мої категорії"),
  ADD_FROM_DEFAULT("Додати існуючу категорію");

  private final String value;

  CategoryAction(String value) {
	this.value = value;
  }

  public String getValue() {
	return value;
  }
}
