package org.expense_bot.helper;

import lombok.RequiredArgsConstructor;
import org.expense_bot.dto.ExpenseGroup;
import org.expense_bot.enums.Button;
import org.expense_bot.enums.CategoryAction;
import org.expense_bot.enums.Period;
import org.expense_bot.model.UserCategory;
import org.expense_bot.service.UserCategoryService;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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

	allCategories.stream().map(Utils::buildKey).forEach(buttonsList::add);
	buttonsList.add(Utils.buildKey(Button.BUTTON_BACK.getValue()));

	return ReplyKeyboardMarkup.builder()
	  .keyboard(buttonsList)
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildMainMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildTwoKeys(Button.EXPENSES.getValue(), Button.INCOMES.getValue())))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }


  public ReplyKeyboard buildExpenseMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildTwoKeys(Button.WRITE_EXPENSES.getValue(), Button.CHECK_EXPENSES.getValue()),
		Utils.buildKey(Button.BUTTON_BACK.getValue())))
	  .selective(true)
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildIncomeMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildKey(Button.WRITE_INCOMES.getValue()),
		Utils.buildKey(Button.CHECK_INCOMES.getValue()),
		Utils.buildKey(Button.CHECK_BALANCE.getValue()),
		Utils.buildKey(Button.BUTTON_BACK.getValue())))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildCheckPeriodMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildKey(Period.DAY.getValue()),
		Utils.buildKey(Period.WEEK.getValue()),
		Utils.buildKey(Period.MONTH.getValue()),
		Utils.buildKey(Period.PERIOD.getValue()),
		Utils.buildKey(Button.BUTTON_BACK.getValue())))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildCheckCategoriesMenu(Long userId) {
	final List<String> categoryList = getCategoryList(userId);

	final List<KeyboardRow> keyboardRows = new ArrayList<>();
	keyboardRows.add(Utils.buildKey(Button.BY_ALL_CATEGORIES.getValue()));
	categoryList.stream().map(Utils::buildKey).forEach(keyboardRows::add);

	keyboardRows.add(Utils.buildKey(Button.BUTTON_BACK.getValue()));
	return ReplyKeyboardMarkup.builder()
	  .keyboard(keyboardRows)
	  .build();
  }

  public ReplyKeyboard buildCategoryOptionsMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildKey(CategoryAction.ADD_NEW_CATEGORY.getValue()),
		Utils.buildKey(CategoryAction.DELETE_MY_CATEGORIES.getValue()),
		Utils.buildKey(CategoryAction.ADD_FROM_DEFAULT.getValue())
	  ))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildCustomCategoriesMenu(List<String> allCategories) {
	final List<KeyboardRow> keyboard = allCategories.stream()
	  .map(Utils::buildKey)
	  .collect(Collectors.toList());
	keyboard.add(Utils.buildKey(Button.BUTTON_BACK.getValue()));

	return ReplyKeyboardMarkup.builder()
	  .keyboard(keyboard)
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildSetDateMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildKey(Button.ENTER_DATE.getValue()),
		Utils.buildKey(Button.BUTTON_BACK.getValue())
	  ))
	  .resizeKeyboard(true)
	  .oneTimeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildBackButton() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(Utils.buildKey(Button.BUTTON_BACK.getValue())))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildSettingsMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(Collections.singletonList(Utils.buildKey(Button.PASSWORD.getValue())))
	  .resizeKeyboard(true)
	  .build();
  }

  private List<String> getCategoryList(Long userId) {
	return userCategoryService.getByUserId(userId)
	  .stream()
	  .map(UserCategory::getCategory)
	  .collect(Collectors.toList());
  }

  public ReplyKeyboard buildExpenseOptions(ExpenseGroup group) {
	final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
	keyboard.setKeyboard(
	  List.of(
		List.of(
		  Utils.buildButton(Button.BUTTON_INFO.getValue(), group.getCategory()))));
	return keyboard;
  }

  public ReplyKeyboard buildCreatePDFMenu() {
	return ReplyKeyboardMarkup.builder()
	  .keyboard(List.of(
		Utils.buildKey(Button.CREATE_PDF.getValue()),
		Utils.buildKey(Button.BUTTON_BACK.getValue())
	  ))
	  .resizeKeyboard(true)
	  .build();
  }

  public ReplyKeyboard buildPasswordOptions(boolean enablePassword) {

	final List<KeyboardRow> passwordExistOptions = List.of(
	  Utils.buildKey(Button.UPDATE_PASSWORD.getValue()),
	  Utils.buildKey(Button.DISABLE_PASSWORD.getValue()),
	  Utils.buildKey(Button.BUTTON_BACK.getValue())
	);

	final List<KeyboardRow> passwordNotExistOptions = List.of(
	  Utils.buildKey(Button.SET_PASSWORD.getValue()),
	  Utils.buildKey(Button.BUTTON_BACK.getValue())
	);

	return ReplyKeyboardMarkup.builder()
	  .keyboard(enablePassword ? passwordExistOptions : passwordNotExistOptions)
	  .resizeKeyboard(true)
	  .build();
  }

}
