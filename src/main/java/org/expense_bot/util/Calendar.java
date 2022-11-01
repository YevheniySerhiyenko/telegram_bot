package org.expense_bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

  public static ReplyKeyboard buildCalendar(Month month) {
	InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
	List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
	keyboardList.add(getFirstLine(month.name()));
	keyboardList.add(getNumbersLine(1, month.length(true)));
	keyboardList.add(getNumbersLine(9, month.length(true)));
	keyboardList.add(getNumbersLine(17, month.length(true)));
	keyboardList.add(getNumbersLine(25, month.length(true)));

	keyboardMarkup.setKeyboard(keyboardList);
	return keyboardMarkup;
  }

  private static List<InlineKeyboardButton> getFirstLine(String month) {
	List<InlineKeyboardButton> buttons = new ArrayList<>();
	InlineKeyboardButton button = new InlineKeyboardButton();
	button.setText("<");
	button.setCallbackData("back");
	InlineKeyboardButton button2 = new InlineKeyboardButton();
	button2.setText(month);
	button2.setCallbackData(month);
	InlineKeyboardButton button3 = new InlineKeyboardButton();
	button3.setText(">");
	button3.setCallbackData("forward");
	buttons.add(button);
	buttons.add(button2);
	buttons.add(button3);
	return buttons;
  }

  private static List<InlineKeyboardButton> getNumbersLine(int number, int numberOfDays) {
	List<InlineKeyboardButton> buttons = new ArrayList<>();
	for (int i = number; i <= numberOfDays; i++) {
	  InlineKeyboardButton button = new InlineKeyboardButton();
	  button.setText(String.valueOf(i));
	  button.setCallbackData(String.valueOf(i));
	  buttons.add(button);
	}
	return buttons;
  }

}
