package expense_bot.enums;

import expense_bot.util.DateUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@RequiredArgsConstructor
public enum Period {
  DAY(Button.DAY.getValue()) {
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getTodayMidnight();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight();
    }
  },
  WEEK(Button.WEEK.getValue()) {
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getStartOfWeek();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight();
    }
  },
  MONTH(Button.MONTH.getValue()) {
    @Override
    public LocalDateTime getDateFrom() {
      return DateUtil.getStartOfMonth();
    }

    @Override
    public LocalDateTime getDateTo() {
      return DateUtil.getTomorrowMidnight();
    }
  },

  PERIOD(Button.PERIOD.getValue()) {
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
