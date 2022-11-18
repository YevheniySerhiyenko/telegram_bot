package org.expense_bot.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Buttons {
  BUTTON_CANCEL("❌"),
  BUTTON_BACK("\uD83D\uDD19"),
  BUTTON_TRASH("\uD83D\uDDD1"),
  BUTTON_INFO("Деталі ℹ"),
  ADD_CATEGORY("➕ Додати свою категорію"),
  DELETE_MY_CATEGORIES("➖ Видалити мої категорії"),
  ADD_FROM_DEFAULT("Додати існуючу категорію"),
  WRITE_EXPENSES("\uD83D\uDCDD Записати витрати"),
  ENTER_DATE("\uD83D\uDCC5 Обрати іншу дату!"),
  BY_ALL_CATEGORIES("По всім категоріям"),
  CHECK_INCOMES("\uD83D\uDCCA Перевірити доходи"),
  WRITE_INCOMES("\uD83D\uDCDD Записати доходи"),
  CREATE_PDF("Створити PDF"),
  CHECK_BALANCE("Перевірити поточний залишок"),
  INCOMES("\uD83D\uDD35 Доходи"),
  EXPENSES("\uD83D\uDD34 Витрати"),
  DAY("За день"),
  WEEK("За тиждень"),
  MONTH("За місяць"),
  PERIOD("За період"),
  CHECK_EXPENSES("\uD83D\uDCCA Перевірити витрати");

  private final String value;

}
