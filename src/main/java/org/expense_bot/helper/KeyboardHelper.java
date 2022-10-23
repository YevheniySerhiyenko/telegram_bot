package org.expense_bot.helper;

import org.expense_bot.enums.CategoryAction;
import org.expense_bot.model.Category;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardHelper {

  public KeyboardHelper() {
  }

  public ReplyKeyboardMarkup buildCategoriesMenu() {
	KeyboardRow row1 = new KeyboardRow();
	row1.add("Книги");
	row1.add("Зв'язок (телефон, інтернет)");

	KeyboardRow row2 = new KeyboardRow();
	row2.add("Побутові потреби");
	row2.add("Шкідливі звички");

	KeyboardRow row3 = new KeyboardRow();
	row3.add("Гігієна та здоров'я");
	row3.add("Кафе");

	KeyboardRow row4 = new KeyboardRow();
	row4.add("Квартплата");
	row4.add("Кредит/борги");

	KeyboardRow row5 = new KeyboardRow();
	row5.add("Одяг та косметика");
	row5.add("Поїздки (транспорт, таксі)");

	KeyboardRow row6 = new KeyboardRow();
	row6.add("Продукти харчування");
	row6.add("Розваги та подарунки");

	KeyboardRow row8 = new KeyboardRow();
	row8.add("❌");
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3, row4, row5, row6, row8))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildMainMenu() {
	KeyboardRow row = new KeyboardRow();
	row.add("Записати витрати");
	row.add("Перевірити витрати");
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false).build();
  }

  public ReplyKeyboardMarkup buildMenuWithCancel() {
	KeyboardRow row = new KeyboardRow();
	row.add("❌");
	return ReplyKeyboardMarkup.builder().keyboard(List.of(row)).selective(true).resizeKeyboard(true).oneTimeKeyboard(false).build();
  }

  public ReplyKeyboardMarkup buildCheckPeriodMenu() {
	KeyboardRow row1 = new KeyboardRow();
	row1.add("За день");
	KeyboardRow row2 = new KeyboardRow();
	row2.add("За тиждень");
	KeyboardRow row3 = new KeyboardRow();
	row3.add("За місяць");
	KeyboardRow row4 = new KeyboardRow();
	row4.add("За період");
	KeyboardRow row5 = new KeyboardRow();
	row5.add("❌");
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3, row4, row5))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCheckCategoriesMenu() {
	KeyboardRow row = new KeyboardRow();
	row.add("По всім категоріям");

	KeyboardRow row1 = new KeyboardRow();
	row1.add("Книги");
	row1.add("Зв'язок (телефон, інтернет)");

	KeyboardRow row2 = new KeyboardRow();
	row2.add("Побутові потреби");
	row2.add("Шкідливі звички");

	KeyboardRow row3 = new KeyboardRow();
	row3.add("Гігієна та здоров'я");
	row3.add("Кафе");

	KeyboardRow row4 = new KeyboardRow();
	row4.add("Квартплата");
	row4.add("Кредит/борги");

	KeyboardRow row5 = new KeyboardRow();
	row5.add("Одяг та косметика");
	row5.add("Поїздки (транспорт, таксі)");

	KeyboardRow row6 = new KeyboardRow();
	row6.add("Продукти харчування");
	row6.add("Розваги та подарунки");

	KeyboardRow row8 = new KeyboardRow();
	row8.add("❌");
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row, row1, row2, row3, row4, row5, row6, row8))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCategoryOptionsMenu() {
	KeyboardRow row1 = new KeyboardRow();
	row1.add(CategoryAction.ADD_NEW_CATEGORY.getValue());
	KeyboardRow row2 = new KeyboardRow();
	row2.add(CategoryAction.DELETE_CATEGORY.getValue());
	KeyboardRow row3 = new KeyboardRow();
	row3.add(CategoryAction.SHOW_MY_CATEGORIES.getValue());
	KeyboardRow row4 = new KeyboardRow();
	row4.add(CategoryAction.ADD_FROM_DEFAULT.getValue());
	KeyboardRow row5 = new KeyboardRow();
	row5.add("❌");
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3, row4, row5))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCustomCategoriesMenu(List<String> allCategories) {
	final List<KeyboardRow> buttonsList = new ArrayList<>();
	for (int i = 0; i < allCategories.size();) {

	  if(i >= allCategories.size()){
		break;
	  }
	  final KeyboardRow row = new KeyboardRow();
	  row.add(allCategories.get(i++));
	  if(i >= allCategories.size()){
		break;
	  }
	  row.add(allCategories.get(i++));

	  if(!row.isEmpty()){
	    buttonsList.add(row);
	  }

	}
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add("❌");
	buttonsList.add(cancelRow);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

}
