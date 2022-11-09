package org.expense_bot.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {

  private static final LocalDate NOW = LocalDate.now();
  private static final int ONE_DAY_VALUE = 1;

  public static LocalDateTime getBeginOfMonth() {
	return LocalDate.of(NOW.getYear(), NOW.getMonth(), ONE_DAY_VALUE).atStartOfDay();
  }

  public static LocalDateTime getTodayMidnight() {
	return LocalDateTime.from(NOW.atStartOfDay());
  }

  public static LocalDateTime getStartOfWeek() {
	final Integer mondayNumber = getCurrentMondayNumber(NOW.getDayOfMonth());
	return LocalDate.of(NOW.getYear(), NOW.getMonth(), mondayNumber).atStartOfDay();
  }

  private static Integer getCurrentMondayNumber(Integer dayOfMonth) {
	final DayOfWeek dayOfWeek = NOW.getDayOfWeek();
	switch (dayOfWeek) {
	  case SUNDAY:
		return dayOfMonth - 6;
	  case SATURDAY:
		return dayOfMonth - 5;
	  case FRIDAY:
		return dayOfMonth - 4;
	  case THURSDAY:
		return dayOfMonth - 3;
	  case WEDNESDAY:
		return dayOfMonth - 2;
	  case TUESDAY:
		return dayOfMonth - 1;
	  default:
		return dayOfMonth;
	}
  }

  public static LocalDateTime getTomorrowMidnight(LocalDate localDate) {
	return localDate.plusDays(ONE_DAY_VALUE).atStartOfDay();
  }

  public static LocalDateTime getTomorrowMidnight() {
	return NOW.plusDays(ONE_DAY_VALUE).atStartOfDay();
  }

  public static LocalDateTime getStartOfMonth() {
	return LocalDate.of(NOW.getYear(), NOW.getMonth(), ONE_DAY_VALUE).atStartOfDay();
  }

}
