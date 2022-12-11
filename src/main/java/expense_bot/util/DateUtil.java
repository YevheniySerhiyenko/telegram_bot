package expense_bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

  private static final int ONE_DAY_VALUE = 1;
  private static final String DATE_PATTERN = "dd.MM.yyyy";
  private static final String TIME_PATTERN = "hh:mm";
  private static final String DATE_TIME_PATTERN = "dd.MM.yyyy";

  public static LocalDateTime getTodayMidnight() {
    return LocalDateTime.from(LocalDate.now().atStartOfDay());
  }

  public static LocalDateTime getStartOfWeek() {
    return LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).atStartOfDay();
  }

  public static LocalDateTime getTomorrowMidnight(LocalDate localDate) {
    return localDate.plusDays(ONE_DAY_VALUE).atStartOfDay();
  }

  public static LocalDateTime getTomorrowMidnight() {
    return LocalDate.now().plusDays(ONE_DAY_VALUE).atStartOfDay();
  }

  public static LocalDateTime getStartOfMonth() {
    return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
  }

  public static String getDate(LocalDateTime localDate) {
    return getFormat(localDate, DATE_PATTERN);
  }

  public static String getDateTime(LocalDateTime localDate) {
    return getFormat(localDate, DATE_TIME_PATTERN);
  }

  public static String getTime(LocalDateTime localDate) {
    return getFormat(localDate, TIME_PATTERN);
  }

  private static String getFormat(LocalDateTime localDate, String timePattern) {
    return localDate.format(DateTimeFormatter.ofPattern(timePattern));
  }

}
