package org.expense_bot.util;

import org.expense_bot.model.UserRequest;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

  public static ReplyKeyboard buildCalendar(LocalDate now) {
	final Month month = now.getMonth();
	final DayOfWeek dayOfWeek = now.getDayOfWeek();
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
	final InlineKeyboardButton button = new InlineKeyboardButton();
	button.setText("<");
	button.setCallbackData("back " + now.getMonth());
	final InlineKeyboardButton button3 = new InlineKeyboardButton();
	button3.setText(">");
	button3.setCallbackData("forward " + now.getMonth());
	buttons.add(button);
	buttons.add(button3);
	return buttons;
  }

  private static List<InlineKeyboardButton> getDateLine(String month, Integer year) {
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	final InlineKeyboardButton button = new InlineKeyboardButton();
	final String date = month + " " + year;
	button.setText(date);
	button.setCallbackData(date);
	buttons.add(button);
	return buttons;
  }

  private static List<InlineKeyboardButton> getNumbersLine(int number, int numberOfDays, LocalDate now) {
	final Month month = now.getMonth();
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	for (int i = number; i <= numberOfDays; i++) {
	  final InlineKeyboardButton button = new InlineKeyboardButton();
	  button.setText(String.valueOf(i));
	  button.setCallbackData(i + "." + month.getValue() + "." + now.getYear());
	  buttons.add(button);
	}
	return buttons;
  }

  public static ReplyKeyboard handleAnotherDate(UserRequest userRequest) {
	if(userRequest.getUpdate().hasCallbackQuery()) {
	  final String data = userRequest.getUpdate().getCallbackQuery().getData();
	  if(data.startsWith("back") || data.startsWith("forward")) {
		final String[] text = data.split(" ");
		final String command = text[0];
		final LocalDate of = getNextOrPreviousDate(text);
		switch (command) {
		  case "back":
			return Calendar.buildCalendar(of.minusMonths(1));
		  case "forward":
			return Calendar.buildCalendar(of.plusMonths(1));
		}
	  }
	}
	return null;
  }

  private static LocalDate getNextOrPreviousDate(String[] text) {
	final Month month = Month.valueOf(text[1]);
	return LocalDate.of(LocalDate.now().getYear(), month, 1);
  }

  public static LocalDate getDate(UserRequest userRequest) {
	if(userRequest.getUpdate().hasCallbackQuery()) {
	  final String date = userRequest.getUpdate().getCallbackQuery().getData();
	  return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	return LocalDate.now();
  }

}
