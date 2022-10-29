package org.expense_bot.helper;

import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.Period;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.CategoryService;
import org.expense_bot.service.UserCategoryService;
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
  private CategoryService categoryService;

  public ReplyKeyboardMarkup buildCategoriesMenu(Long userId) {

	List<KeyboardRow> buttonsList = new ArrayList<>();

	final List<String> allCategories = userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());

	setButtons(buttonsList, allCategories);
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add(Constants.BUTTON_BACK);
	buttonsList.add(cancelRow);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }


  public ReplyKeyboardMarkup buildMainMenu() {
	KeyboardRow row = new KeyboardRow();
	row.add(Messages.WRITE_EXPENSES);
	row.add(Messages.CHECK_EXPENSES);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false).build();
  }

  public ReplyKeyboardMarkup buildCategoryActionsMenuWithCancel() {
	final ReplyKeyboardMarkup replyKeyboardMarkup = buildCategoryOptionsMenu();
	KeyboardRow row = new KeyboardRow();
	row.add(Constants.BUTTON_CANCEL);
	final List<KeyboardRow> keyboard = replyKeyboardMarkup.getKeyboard();
	keyboard.add(row);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(keyboard)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();

  }

  public ReplyKeyboardMarkup buildMenuWithCancel() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Constants.BUTTON_CANCEL);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
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
	row5.add(Constants.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3, row4, row5))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCheckCategoriesMenu(Long userId) {
	final List<String> categoryList = userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());

	final List<KeyboardRow> keyboardRows = new ArrayList<>();
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.BY_ALL_CATEGORIES);
	keyboardRows.add(row);
	setButtons(keyboardRows, categoryList);


	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Constants.BUTTON_BACK);
	keyboardRows.add(row1);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(keyboardRows)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCategoryOptionsMenu() {
	KeyboardRow row1 = new KeyboardRow();
	row1.add(CategoryAction.ADD_NEW_CATEGORY.getValue());
	KeyboardRow row2 = new KeyboardRow();
	row2.add(CategoryAction.SHOW_MY_CATEGORIES.getValue());
	KeyboardRow row3 = new KeyboardRow();
	row3.add(CategoryAction.ADD_FROM_DEFAULT.getValue());
	KeyboardRow row4 = new KeyboardRow();
	row4.add(Constants.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3, row4))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCustomCategoriesMenu(List<String> allCategories) {
	final List<KeyboardRow> buttonsList = new ArrayList<>();
	setButtons(buttonsList, allCategories);
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add(Constants.BUTTON_BACK);
	buttonsList.add(cancelRow);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  private void setButtons(List<KeyboardRow> buttonsList, List<String> allCategories) {
	for (String category : allCategories) {
	  final KeyboardRow row = new KeyboardRow();
	  row.add(category);
	  buttonsList.add(row);
	}
  }

  public ReplyKeyboardMarkup buildСonfirm() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.SUCCESS);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboardMarkup buildSetDateMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.ENTER_DATE);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboardMarkup buildBackButtonMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Constants.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboardMarkup buildSettingsMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.MY_STICKERS);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

}
