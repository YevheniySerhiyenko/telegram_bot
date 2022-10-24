package org.expense_bot.helper;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.Period;
import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeyboardHelper {

  @Autowired
  private UserCategoryService userCategoryService;
  @Autowired
  private UserService userService;

  public ReplyKeyboardMarkup buildCategoriesMenu(Long userId) {
	final User user = userService.getByChatId(userId)
	  .orElseThrow(() -> new RuntimeException("User not found"));

	List<KeyboardRow> buttonsList = new ArrayList<>();

	final List<String> allCategories = userCategoryService.getByUser(user)
	  .stream()
	  .map(UserCategory::getCategory)
	  .map(Category::getName)
	  .collect(Collectors.toList());

	setButtons(buttonsList, allCategories);
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

  private void setButtons(List<KeyboardRow> buttonsList, List<String> allCategories) {
	for (int i = 0; i < allCategories.size(); ) {

	  final KeyboardRow row = new KeyboardRow();
	  row.add(allCategories.get(i++));
	  if(i >= allCategories.size()) {
		break;
	  }
	  row.add(allCategories.get(i++));

	  if(!row.isEmpty()) {
		buttonsList.add(row);
	  }

	}
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
	row1.add(Period.DAY.getValue());
	KeyboardRow row2 = new KeyboardRow();
	row2.add(Period.WEEK.getValue());
	KeyboardRow row3 = new KeyboardRow();
	row3.add(Period.MONTH.getValue());
	KeyboardRow row4 = new KeyboardRow();
	row4.add(Period.PERIOD.getValue());
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
	setButtons(buttonsList, allCategories);
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
