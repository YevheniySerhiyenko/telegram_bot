package org.expense_bot.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

  private static final LocalDate NOW = LocalDate.now();
  private static final int ONE_DAY_VALUE = 1;
  private static final String DATE_PATTERN = "dd.MM.yyyy";
  private static final String DATE_TIME_PATTERN = "dd.MM.yyyy hh:mm:SS";

  public static LocalDateTime getTodayMidnight() {
	return LocalDateTime.from(NOW.atStartOfDay());
  }

  public static LocalDateTime getStartOfWeek() {
	final Integer mondayNumber = getCurrentMondayNumber(NOW.getDayOfMonth());
	return LocalDate.of(NOW.getYear(), NOW.getMonth(), mondayNumber).atStartOfDay();
  }

  private static Integer getCurrentMondayNumber(Integer dayOfMonth) {
	final DayOfWeek dayOfWeek = NOW.getDayOfWeek();
	if(dayOfWeek == DayOfWeek.SUNDAY) {
	  return dayOfMonth - 6;
	} else if(dayOfWeek == DayOfWeek.SATURDAY) {
	  return dayOfMonth - 5;
	} else if(dayOfWeek == DayOfWeek.FRIDAY) {
	  return dayOfMonth - 4;
	} else if(dayOfWeek == DayOfWeek.THURSDAY) {
	  return dayOfMonth - 3;
	} else if(dayOfWeek == DayOfWeek.WEDNESDAY) {
	  return dayOfMonth - 2;
	} else if(dayOfWeek == DayOfWeek.TUESDAY) {
	  return dayOfMonth - 1;
	}
	return dayOfMonth;
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

  public static String getDate(LocalDateTime localDate) {
	return localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
  }

  public static String getDateTime(LocalDateTime localDate) {
	return localDate.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
  }
}
