package org.expense_bot.util;

import org.expense_bot.model.Request;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.expense_bot.handler.RequestHandler.getUpdateData;
import static org.expense_bot.handler.RequestHandler.hasCallBack;

public class Calendar {

  private static final String PATTERN = "dd.MM.yyyy";

  public static InlineKeyboardMarkup buildCalendar(LocalDate now) {
	final Month month = now.getMonth();
	final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
	final int numberOfDays = month.length(true);
	final List<List<InlineKeyboardButton>> keyboardList = List.of(
	  getDateLine(month.name(), now.getYear()),
	  getNumbersLine(1, 8, now),
	  getNumbersLine(9, 16, now),
	  getNumbersLine(17, 24, now),
	  getNumbersLine(25, numberOfDays, now),
	  getLastLine(now)
	);

	keyboardMarkup.setKeyboard(keyboardList);
	return keyboardMarkup;
  }

  private static List<InlineKeyboardButton> getLastLine(LocalDate now) {
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	buttons.add(Utils.buildButton("<", "back " + now.getMonth() + " " + now.getYear()));
	buttons.add(Utils.buildButton(">", "forward " + now.getMonth() + " " + now.getYear()));
	return buttons;
  }

  private static List<InlineKeyboardButton> getDateLine(String month, Integer year) {
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	final String date = month + " " + year;
	buttons.add(Utils.buildButton(date, date));
	return buttons;
  }

  private static List<InlineKeyboardButton> getNumbersLine(int number, int numberOfDays, LocalDate now) {
	return IntStream.rangeClosed(number, numberOfDays)
	  .mapToObj(num -> Utils.buildButton(String.valueOf(num), getCallbackData(now, num)))
	  .collect(Collectors.toList());
  }

  private static String getCallbackData(LocalDate now, int number) {
	final String callBackDayValue = getCallBackFormatValue(String.valueOf(number));
	final String callBackMonthValue = getCallBackFormatValue(String.valueOf(now.getMonth().getValue()));
	return callBackDayValue + "." + callBackMonthValue + "." + now.getYear();
  }

  private static String getCallBackFormatValue(String value) {
	return value.length() == 1 ? "0" + value : value;
  }

  public static InlineKeyboardMarkup changeMonth(Request request) {
	if(hasCallBack(request)) {
	  final String data = getUpdateData(request);
	  if(data.startsWith("back") || data.startsWith("forward")) {
		final String[] text = data.split(" ");
		final String command = text[0];
		final LocalDate of = getNextOrPreviousMonth(text);
		switch (command) {
		  case "back":
			return Calendar.buildCalendar(of.minusMonths(1));
		  case "forward":
			return Calendar.buildCalendar(of.plusMonths(1));
		  default:
			return null;
		}
	  }
	}
	return null;
  }

  public static InlineKeyboardMarkup changeYear(Request request) {
	if(hasCallBack(request)) {
	  final String data = getUpdateData(request);
	  if(data.startsWith("back") || data.startsWith("forward")) {
		final String[] text = data.split(" ");
		final String command = text[0];
		final LocalDate of = getNextOrPreviousYear(text);
		switch (command) {
		  case "back":
			return Calendar.buildMonthCalendar(of.minusYears(1));
		  case "forward":
			return Calendar.buildMonthCalendar(of.plusYears(1));
		  default:
			return null;
		}
	  }
	}
	return null;
  }

  private static LocalDate getNextOrPreviousYear(String[] text) {
	final Month month = Month.valueOf(text[1]);
	final int year = Integer.parseInt(text[2]);
	return LocalDate.of(year, month,1);
  }

  private static LocalDate getNextOrPreviousMonth(String[] text) {
	final Month month = Month.valueOf(text[1]);
	return LocalDate.of(LocalDate.now().getYear(), month, 1);
  }

  public static LocalDate getDate(Request request) {
	if(hasCallBack(request)) {
	  return LocalDate.parse(getUpdateData(request), DateTimeFormatter.ofPattern(PATTERN));
	}
	return LocalDate.now();
  }

  public static InlineKeyboardMarkup buildMonthCalendar(LocalDate date) {
	final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
	final int year = date.getYear();
	final List<List<InlineKeyboardButton>> keyboardList = List.of(
	  getDateLine("", year),
	  getMonths(0, 2, year),
	  getMonths(3, 5, year),
	  getMonths(6,8, year),
	  getMonths(9,11, year),
	  getLastLine(date)
	);

	keyboardMarkup.setKeyboard(keyboardList);
	return keyboardMarkup;
  }

  private static List<InlineKeyboardButton> getMonths(Integer startMonth, Integer lastMonth, Integer year) {
	final List<InlineKeyboardButton> buttonsMonths = new ArrayList<>();
	final Month[] values = Month.values();
	for (int i = startMonth; i <= lastMonth; i++) {
	  final String data = String.valueOf(values[i]);
	  buttonsMonths.add(Utils.buildButton(data, data + " " + year));
	}
	return buttonsMonths;
  }

  public static LocalDate parseMonthYear(String date){
	final String[] monthYear = date.split(" ");
	final Month month = Month.valueOf(monthYear[0]);
	final int year = Integer.parseInt(monthYear[1]);
	return LocalDate.of(year,month,1);
  }

}
