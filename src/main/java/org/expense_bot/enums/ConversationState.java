package org.expense_bot.enums;

import java.util.Arrays;
import java.util.Optional;

public interface ConversationState {


  static ConversationState getPreviousState(String state) {

	final Optional<ConversationState> first = Arrays.stream(Settings.values())
	  .filter(stat -> stat.name().equals(state))
	  .map(Enum::ordinal)
	  .map(Settings::getByOrdinal).findFirst();
	final Optional<ConversationState> first1 = Arrays.stream(Expenses.values())
	  .filter(stat -> stat.name().equals(state))
	  .map(Enum::ordinal)
	  .map(Expenses::getByOrdinal).findFirst();
	final Optional<ConversationState> first3 = Arrays.stream(Categories.values())
	  .filter(stat -> stat.name().equals(state))
	  .map(Enum::ordinal)
	  .map(Categories::getByOrdinal).findFirst();
	final Optional<ConversationState> first4 = Arrays.stream(Incomes.values())
	  .filter(stat -> stat.name().equals(state))
	  .map(Enum::ordinal)
	  .map(Incomes::getByOrdinal).findFirst();
	final ConversationState other = first4.orElse(
	  Arrays.stream(Init.values())
		.filter(stat -> stat.name().equals(state))
		.map(Enum::ordinal)
		.map(Init::getByOrdinal).findFirst().orElse(Init.CONVERSATION_STARTED)
	);
	return first
	  .orElse(first1
		.orElse(first3.orElse(other)));
  }

  enum Init implements ConversationState {
	CONVERSATION_STARTED,
	WAITING_INIT_ACTION,
	WAITING_EXPENSE_ACTION,
	WAITING_INCOME_ACTION,
	WAITING_SETTINGS_ACTION;

	public static ConversationState getByOrdinal(Integer ordinal) {
	  return Arrays.stream(Init.values())
		.filter(state -> state.ordinal() == ordinal - 1)
		.findFirst().orElse(null);
	}

  }

  enum Expenses implements ConversationState {
	//check
	WAITING_EXPENSE_ACTION,
	WAITING_FOR_PERIOD,
	WAITING_FOR_TWO_DATES,
	WAITING_CHECK_CATEGORY,
	WAITING_ADDITIONAL_ACTION,

	//write,
	WAITING_FOR_CATEGORY,
	WAITING_FOR_ANOTHER_EXPENSE_DATE,
	WAITING_FOR_EXPENSE_SUM;

	public static ConversationState getByOrdinal(Integer ordinal) {
	  return Arrays.stream(Init.values())
		.filter(state -> state.ordinal() == ordinal - 1)
		.findFirst().orElse(null);
	}
  }


  enum Incomes implements ConversationState {
	WAITING_FOR_INCOME_SUM,
	WAITING_INCOME_ACTION,
	WAITING_FOR_ANOTHER_INCOME_DATE,
	WAITING_FOR_PERIOD;

	public static ConversationState getByOrdinal(Integer ordinal) {
	  return Arrays.stream(Incomes.values())
		.filter(state -> state.ordinal() == ordinal - 1)
		.findFirst()
		.orElse(null);
	}
  }

  enum Categories implements ConversationState {
	WAITING_CATEGORY_ACTION,
	WAITING_FINAL_ACTION;

	public static ConversationState getByOrdinal(Integer ordinal) {
	  return Arrays.stream(Categories.values())
		.filter(state -> state.ordinal() == ordinal - 1)
		.findFirst()
		.orElse(null);
	}
  }

  enum Settings implements ConversationState {
	WAITING_SETTINGS_ACTION,
	WAITING_FINAL_SETTING_ACTION;

	public static ConversationState getByOrdinal(Integer ordinal) {
	  return Arrays.stream(Settings.values())
		.filter(state -> state.ordinal() == ordinal - 1)
		.findFirst()
		.orElse(null);
	}
  }

}
