package org.expense_bot.helper;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.constant.Messages;
import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.Period;
import org.expense_bot.model.Sticker;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.util.Utils;
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
@RequiredArgsConstructor
public class KeyboardBuilder {

  private final UserCategoryService userCategoryService;

  public ReplyKeyboard buildCategoriesMenu(Long userId) {
	final List<KeyboardRow> buttonsList = new ArrayList<>();
	final List<String> allCategories = getCategoryList(userId);

	setButtons(buttonsList, allCategories);
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add(Buttons.BUTTON_BACK);
	buttonsList.add(cancelRow);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false)
	  .build();
  }

  public ReplyKeyboard buildMainMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.EXPENSES);
	row.add(Messages.INCOMES);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(false).build();
  }


  public ReplyKeyboard buildExpenseMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.WRITE_EXPENSES);
	row.add(Messages.CHECK_EXPENSES);
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Buttons.BUTTON_BACK);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row, row1))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildIncomeMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.WRITE_INCOMES);
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Messages.CHECK_INCOMES);
	final KeyboardRow row2 = new KeyboardRow();
	row2.add(Messages.CHECK_BALANCE);
	final KeyboardRow row3 = new KeyboardRow();
	row3.add(Buttons.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row,row1, row2, row3))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildCheckPeriodMenu() {
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Period.DAY.getValue());
	final KeyboardRow row2 = new KeyboardRow();
	row2.add(Period.WEEK.getValue());
	final KeyboardRow row3 = new KeyboardRow();
	row3.add(Period.MONTH.getValue());
	final KeyboardRow row4 = new KeyboardRow();
	row4.add(Period.PERIOD.getValue());
	final KeyboardRow row5 = new KeyboardRow();
	row5.add(Buttons.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3, row4, row5))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildCheckCategoriesMenu(Long userId) {
	final List<String> categoryList = getCategoryList(userId);

	final List<KeyboardRow> keyboardRows = new ArrayList<>();
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.BY_ALL_CATEGORIES);
	keyboardRows.add(row);
	setButtons(keyboardRows, categoryList);

	final KeyboardRow row1 = new KeyboardRow();
	row1.add(Buttons.BUTTON_BACK);
	keyboardRows.add(row1);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(keyboardRows)
	  .build();
  }

  public ReplyKeyboard buildCategoryOptionsMenu() {
	final KeyboardRow row1 = new KeyboardRow();
	row1.add(CategoryAction.ADD_NEW_CATEGORY.getValue());
	final KeyboardRow row2 = new KeyboardRow();
	row2.add(CategoryAction.DELETE_MY_CATEGORIES.getValue());
	final KeyboardRow row3 = new KeyboardRow();
	row3.add(CategoryAction.ADD_FROM_DEFAULT.getValue());
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row1, row2, row3))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildCustomCategoriesMenu(List<String> allCategories) {
	final List<KeyboardRow> buttonsList = new ArrayList<>();
	setButtons(buttonsList, allCategories);
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add(Buttons.BUTTON_BACK);
	buttonsList.add(cancelRow);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .resizeKeyboard(true)
	  .build();
  }

  private void setButtons(List<KeyboardRow> buttonsList, List<String> allCategories) {
	for (String category : allCategories) {
	  final KeyboardRow row = new KeyboardRow();
	  row.add(category);
	  buttonsList.add(row);
	}
  }

  public ReplyKeyboard buildSetDateMenu() {
	final KeyboardRow row = new KeyboardRow();
	final KeyboardRow row1 = new KeyboardRow();
	row.add(Messages.ENTER_DATE);
	row1.add(Buttons.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row, row1))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildBackButton() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Buttons.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(row))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildSettingsMenu() {
	final KeyboardRow row = new KeyboardRow();
	row.add(Messages.MY_STICKERS);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(Collections.singletonList(row))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildStickerOptions(String action) {
	final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
	final List<List<InlineKeyboardButton>> keyboards = new ArrayList<>();
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	final InlineKeyboardButton button = new InlineKeyboardButton();
	button.setText("Вимкнути");
	button.setCallbackData(action);
	final InlineKeyboardButton button2 = new InlineKeyboardButton();
	button2.setText("Змінити");
	button2.setCallbackData(action);
	buttons.add(button);
	buttons.add(button2);
	keyboards.add(buttons);
	keyboard.setKeyboard(keyboards);
	return keyboard;
  }

  public ReplyKeyboard buildStickersActionMenu(List<Sticker> actualStickers) {
	final List<KeyboardRow> buttonsList = new ArrayList<>();
	final KeyboardRow cancelRow = new KeyboardRow();
	cancelRow.add(Buttons.BUTTON_BACK);
	setStickerActionButtons(buttonsList, actualStickers);
	buttonsList.add(cancelRow);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .resizeKeyboard(true)
	  .build();
  }

  private void setStickerActionButtons(List<KeyboardRow> buttonsList, List<Sticker> stickers) {
	for (Sticker sticker : stickers) {
	  final KeyboardRow row = new KeyboardRow();
	  row.add(sticker.getAction());
	  row.add(Buttons.BUTTON_ON);
	  buttonsList.add(row);
	}
  }

  private List<String> getCategoryList(Long userId) {
	return userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

  public ReplyKeyboard buildExpenseOptions(ExpenseGroup group) {
	final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
	final List<List<InlineKeyboardButton>> keyboards = new ArrayList<>();
	final List<InlineKeyboardButton> buttons = new ArrayList<>();
	buttons.add(Utils.buildButton(Buttons.BUTTON_INFO, group.getCategory()));
	keyboards.add(buttons);
	inlineKeyboardMarkup.setKeyboard(keyboards);
	return inlineKeyboardMarkup;
  }

  public ReplyKeyboard buildCreatePDFMenu() {
	final KeyboardRow createPDFButton = new KeyboardRow();
	createPDFButton.add(Messages.CREATE_PDF);
	final KeyboardRow backButton = new KeyboardRow();
	backButton.add(Buttons.BUTTON_BACK);
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(createPDFButton, backButton))
	  .resizeKeyboard(true)
	  .build();
  }

}
