package expense_bot.enums;

import expense_bot.constant.ErrorMessages;
import expense_bot.handler.categories.action_state.CategoryActionState;
import expense_bot.handler.categories.action_state.CategoryAddNewHandler;
import expense_bot.handler.categories.action_state.CategoryDefaultHandler;
import expense_bot.handler.categories.action_state.CategoryDeleteHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CategoryAction {

  ADD_NEW_CATEGORY(Button.ADD_CATEGORY.getValue(), CategoryAddNewHandler.class),
  DELETE_MY_CATEGORIES(Button.DELETE_MY_CATEGORIES.getValue(), CategoryDeleteHandler.class),
  ADD_FROM_DEFAULT(Button.ADD_FROM_DEFAULT.getValue(), CategoryDefaultHandler.class);

  private final String value;
  private final Class<? extends CategoryActionState> handler;

  public static CategoryAction parseAction(String text) {
    return Arrays.stream(values())
      .filter(action -> action.getValue().equals(text))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PARSE_CATEGORY_ACTION_ERROR));

  }
}
