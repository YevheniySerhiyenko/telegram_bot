package org.expense_bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

  public static ReplyKeyboard buildCalendar(LocalDate now) {
	final Month month = now.getMonth();
	final DayOfWeek dayOfWeek = now.getDayOfWeek();
	final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
	final List<List<InlineKeyboardButton>> keyboardList = List.of(
	  getDateLine(month.name(), now.getYear()),
	  getNumbersLine(1, month.length(true)),
	  getNumbersLine(9, month.length(true)),
	  getNumbersLine(17, month.length(true)),
	  getNumbersLine(25, month.length(true)),
	  getLastLine()
	);

	keyboardMarkup.setKeyboard(keyboardList);
	return keyboardMarkup;
  }

  private static List<InlineKeyboardButton> getLastLine() {
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	final InlineKeyboardButton button = new InlineKeyboardButton();
	button.setText("<");
	button.setCallbackData("back");
	final InlineKeyboardButton button3 = new InlineKeyboardButton();
	button3.setText(">");
	button3.setCallbackData("forward");
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

  private static List<InlineKeyboardButton> getNumbersLine(int number, int numberOfDays) {
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	for (int i = number; i <= numberOfDays; i++) {
	  final InlineKeyboardButton button = new InlineKeyboardButton();
	  button.setText(String.valueOf(i));
	  button.setCallbackData(String.valueOf(i));
	  buttons.add(button);
	}
	return buttons;
  }

}
