package org.expense_bot.enums;

public enum Period {
  DAY("За день"),
  WEEK("За тиждень"),
  MONTH("За місяць" ),
  PERIOD("За період");

  private String value;

  Period(String value) {
	this.value = value;
  }

  public String getValue() {
	return value;
  }
}
