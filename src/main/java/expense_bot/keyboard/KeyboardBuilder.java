package expense_bot.keyboard;

import expense_bot.dto.ExpenseGroup;
import expense_bot.enums.Button;
import expense_bot.enums.CategoryAction;
import expense_bot.enums.Period;
import expense_bot.util.Utils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyboardBuilder {

  public static ReplyKeyboard buildMainMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildTwoKeys(
        Button.EXPENSES.getValue(),
        Button.INCOMES.getValue())
      )
      .selective(true)
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }


  public static ReplyKeyboard buildExpenseMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildTwoKeys(
        Button.WRITE_EXPENSES.getValue(),
        Button.CHECK_EXPENSES.getValue())
      )
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .selective(true)
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildIncomeMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildKey(Button.WRITE_INCOMES.getValue()))
      .keyboardRow(Utils.buildKey(Button.CHECK_INCOMES.getValue()))
      .keyboardRow(Utils.buildKey(Button.CHECK_BALANCE.getValue()))
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildCheckPeriodMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildKey(Period.DAY.getValue()))
      .keyboardRow(Utils.buildKey(Period.WEEK.getValue()))
      .keyboardRow(Utils.buildKey(Period.MONTH.getValue()))
      .keyboardRow(Utils.buildKey(Period.PERIOD.getValue()))
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildCategoryOptionsMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildKey(CategoryAction.ADD_NEW_CATEGORY.getValue()))
      .keyboardRow(Utils.buildKey(CategoryAction.DELETE_MY_CATEGORIES.getValue()))
      .keyboardRow(Utils.buildKey(CategoryAction.ADD_FROM_DEFAULT.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildCategories(List<String> categories) {
    final List<KeyboardRow> keyboard = categories.stream()
      .map(Utils::buildKey)
      .collect(Collectors.toList());

    return ReplyKeyboardMarkup.builder()
      .keyboard(keyboard)
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .resizeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildSetDateMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildKey(Button.ENTER_DATE.getValue()))
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildBackButton() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildSettingsMenu() {
    return ReplyKeyboardMarkup.builder()
      .keyboardRow(Utils.buildKey(Button.PASSWORD.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }



  public static ReplyKeyboard buildExpenseOptions(ExpenseGroup group) {
    return InlineKeyboardMarkup.builder()
      .keyboardRow(List.of(
        Utils.buildButton(Button.BUTTON_INFO.getValue(), group.getCategory())))
      .build();
  }

  public static ReplyKeyboard buildCreatePDFMenu() {
    return ReplyKeyboardMarkup.builder()
//      .keyboardRow(Utils.buildKey(Button.CREATE_PDF.getValue()))
      .keyboardRow(Utils.buildKey(Button.BUTTON_BACK.getValue()))
      .resizeKeyboard(true)
      .oneTimeKeyboard(true)
      .build();
  }

  public static ReplyKeyboard buildPasswordOptions(boolean enablePassword) {
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
      .oneTimeKeyboard(true)
      .build();
  }

}
