package org.expense_bot.enums;

import org.expense_bot.constant.Messages;

public enum Period {
  DAY(Messages.DAY),
  WEEK(Messages.WEEK),
  MONTH(Messages.MONTH),
  PERIOD(Messages.PERIOD);

  private final String value;

  Period(String value) {
	this.value = value;
  }

  public String getValue() {
	return value;
  }
}
