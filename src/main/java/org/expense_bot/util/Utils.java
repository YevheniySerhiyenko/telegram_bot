package org.expense_bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.expense_bot.model.Request;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.expense_bot.handler.RequestHandler.getUpdateData;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

  public static String getFileId(final Message message) {

	if(message.hasPhoto()) {
	  return message.getPhoto().get(0).getFileId();
	} else if(message.hasVideo()) {
	  return message.getVideo().getFileId();
	} else if(message.hasVideoNote()) {
	  return message.getVideoNote().getFileId();
	} else if(message.hasDocument()) {
	  return message.getDocument().getFileId();
	} else if(message.hasAudio()) {
	  return message.getAudio().getFileId();
	}
	return null;
  }


  public static InlineKeyboardButton buildButton(String text, String callback) {
	return InlineKeyboardButton.builder().text(text).callbackData(callback).build();
  }

  public static InlineKeyboardMarkup getOneButtonBoard(String text, String callback) {
	final List<InlineKeyboardButton> button = singletonList(buildButton(text, callback));
	return InlineKeyboardMarkup.builder().keyboardRow(button).build();
  }

  public static InlineKeyboardMarkup getManyOneButtonBoard(Map<String, String> buttons) {
	InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

	buttons.entrySet().stream()
	  .map(button -> List.of(Utils.buildButton(button.getKey(), button.getValue())))
	  .forEach(keyboardBuilder::keyboardRow);

	return keyboardBuilder.build();
  }

  public static KeyboardRow buildKey(String action) {
	KeyboardRow keyboardRow = new KeyboardRow();
	keyboardRow.add(action);
	return keyboardRow;
  }

  public static KeyboardRow buildTwoKeys(String firstAction, String secondAction) {
	KeyboardRow keyboardRow = new KeyboardRow();
	keyboardRow.add(firstAction);
	keyboardRow.add(secondAction);
	return keyboardRow;
  }

  public static Optional<BigDecimal> checkWrongSum(Request request) {
	return Optional.of(new BigDecimal(getUpdateData(request)));
  }

}
