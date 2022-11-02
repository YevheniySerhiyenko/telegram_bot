package org.expense_bot.helper;

import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.Period;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeyboardHelper {

  @Autowired
  private UserCategoryService userCategoryService;

  public ReplyKeyboardMarkup buildCategoriesMenu(Long userId) {

	final List<KeyboardRow> buttonsList = new ArrayList<>();

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
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.EXPENSES);
	row.add(Messages.INCOMES);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false).build();
  }


  public ReplyKeyboardMarkup buildExpenseMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.WRITE_EXPENSES);
	row.add(Messages.CHECK_EXPENSES);
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Constants.BUTTON_BACK);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row, row1))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false).build();
  }

  public ReplyKeyboardMarkup buildIncomeMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.WRITE_INCOMES);
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Messages.CHECK_INCOMES);
	final KeyboardRow row2 = new KeyboardRow();
	row2.add(Messages.CHECK_BALANCE);
	final KeyboardRow row3 = new KeyboardRow();
	row3.add(Constants.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row,row1, row2, row3))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false).build();
  }

  public ReplyKeyboardMarkup buildMenuWithCancel() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Constants.BUTTON_CANCEL);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(Collections.singletonList(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboardMarkup buildCheckPeriodMenu() {
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Period.DAY.getValue());
	final KeyboardRow row2 = new KeyboardRow();
	row2.add(Period.WEEK.getValue());
	final KeyboardRow row3 = new KeyboardRow();
	row3.add(Period.MONTH.getValue());
	final KeyboardRow row4 = new KeyboardRow();
	row4.add(Period.PERIOD.getValue());
	final KeyboardRow row5 = new KeyboardRow();
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
	row2.add(CategoryAction.DELETE_MY_CATEGORIES.getValue());
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

  public ReplyKeyboardMarkup buildSetDateMenu() {
	final KeyboardRow row = new KeyboardRow();
	final KeyboardRow row1 = new KeyboardRow();
	row.add(Messages.ENTER_DATE);
	row1.add(Constants.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row, row1))
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
	  .keyboard(Collections.singletonList(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildStickerOptions(String action) {
	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
	List<List<InlineKeyboardButton>> keyboards = new ArrayList<>();
	List<InlineKeyboardButton> buttons = new ArrayList<>();
	InlineKeyboardButton button = new InlineKeyboardButton();
	button.setText("Вимкнути");
	button.setCallbackData(action);
	InlineKeyboardButton button2 = new InlineKeyboardButton();
	button2.setText("Змінити");
	button2.setCallbackData(action);
	buttons.add(button);
	buttons.add(button2);
	keyboards.add(buttons);
	inlineKeyboardMarkup.setKeyboard(keyboards);
	return inlineKeyboardMarkup;
  }

  public ReplyKeyboardMarkup buildStickersActionMenu(List<Sticker> actualStickers) {
	final List<KeyboardRow> buttonsList = new ArrayList<>();
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add(Constants.BUTTON_BACK);
	setStickerActionButtons(buttonsList, actualStickers);
	buttonsList.add(cancelRow);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  private void setStickerActionButtons(List<KeyboardRow> buttonsList, List<Sticker> stickers) {
	for (Sticker sticker : stickers) {
	  final KeyboardRow row = new KeyboardRow();
	  row.add(sticker.getAction());
	  row.add(Constants.BUTTON_ON);
	  buttonsList.add(row);
	}
  }

}
