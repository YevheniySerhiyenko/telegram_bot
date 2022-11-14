package org.expense_bot.enums;

import org.expense_bot.constant.Messages;
import org.expense_bot.util.DateUtil;

import java.time.LocalDate;
import java.util.Arrays;

public enum Period {
  DAY(Messages.DAY) {
    @Override
    public LocalDate getPeriodStart() {
      return DateUtil.getTodayMidnight().toLocalDate();
    }
  },
  WEEK(Messages.WEEK){
    @Override
    public LocalDate getPeriodStart() {
      return null;
    }
  }
  MONTH(Messages.MONTH),
  PERIOD(Messages.PERIOD);

  private final String value;

  Period(String value) {
    this.value = value;
  }

  public static Period parsePeriod(String period) {
    return Arrays.stream(values())
      .filter(enm -> enm.getValue().equals(period))
      .findFirst()
      .orElse(null);
  }

  public String getValue() {
    return value;
  }

  public abstract LocalDate getPeriodStart();
}
