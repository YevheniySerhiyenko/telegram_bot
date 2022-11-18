package org.expense_bot.enums;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@RequiredArgsConstructor
public enum Period {
  DAY(Buttons.DAY.getValue()) {
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getTodayMidnight();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight();
    }
  },
  WEEK(Buttons.WEEK.getValue()){
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getStartOfWeek();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight(LocalDate.now());
    }
  },
  MONTH(Buttons.MONTH.getValue()){
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getStartOfMonth();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight(LocalDate.now());
    }
  },

  PERIOD(Buttons.PERIOD.getValue()){
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

  public static Period parsePeriod(String period) {
    return Arrays.stream(values())
      .filter(enm -> enm.getValue().equals(period))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unable to parse period"));
  }

  public String getValue() {
    return value;
  }

  public abstract LocalDateTime getDateFrom();

  public abstract LocalDateTime getDateTo();
}
