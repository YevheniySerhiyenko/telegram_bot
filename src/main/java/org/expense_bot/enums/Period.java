package org.expense_bot.enums;

import org.expense_bot.constant.Messages;
import org.expense_bot.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public enum Period {
  DAY(Messages.DAY) {
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getTodayMidnight();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight();
    }
  },
  WEEK(Messages.WEEK){
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getStartOfWeek();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight(LocalDate.now());
    }
  },
  MONTH(Messages.MONTH){
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getStartOfMonth();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight(LocalDate.now());
    }
  },

  PERIOD(Messages.PERIOD){
    @Override
    public LocalDateTime getDateFrom() {
      return null;
    }

    @Override
    public LocalDateTime getDateTo() {
      return null;
    }
  };

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

  public abstract LocalDateTime getDateFrom();

  public abstract LocalDateTime getDateTo();
}
