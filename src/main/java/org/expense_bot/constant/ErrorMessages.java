package org.expense_bot.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {

  public static final String PARSE_EXPENSE_ACTION_ERROR = "Unable to parse expense action";

  public static final String PARSE_INIT_ACTION_ERROR = "Unable to parse init action";

  public static final String PARSE_INCOME_ACTION_ERROR = "Unable to parse income action";

  public static final String PARSE_PASSWORD_ACTION_ERROR = "Unable to parse password action";

  public static final String PARSE_CATEGORY_ACTION_ERROR = "Unable to parse category action";

  public static final String USER_NOT_FOUND = "User not found by id ";
}
