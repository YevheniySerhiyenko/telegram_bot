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

  public static Period parsePeriod(String period) {
    switch (period) {
      case Messages.DAY:
        return Period.DAY;
      case Messages.WEEK:
        return Period.WEEK;
      case Messages.MONTH:
        return Period.MONTH;
      case Messages.PERIOD:
        return Period.PERIOD;
      default:
        return null;
    }
  }

  public String getValue() {
    return value;
  }
}
