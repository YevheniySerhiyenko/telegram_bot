package org.expense_bot.util;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Collections.singletonList;

public class Utils {

  public static String getFileId(final Message message) {
    if (message == null) {
      return null;
    }

    String fileId = null;
    if (message.hasPhoto()) {
      fileId = message.getPhoto().get(0).getFileId();
    } else if (message.hasVideo()) {
      fileId = message.getVideo().getFileId();
    } else if (message.hasVideoNote()) {
      fileId = message.getVideoNote().getFileId();
    } else if (message.hasDocument()) {
      fileId = message.getDocument().getFileId();
    } else if (message.hasAudio()) {
      fileId = message.getAudio().getFileId();
    }
    return fileId;
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

    for (Entry<String, String> button : buttons.entrySet()) {
      keyboardBuilder.keyboardRow(
          List.of(Utils.buildButton(button.getKey(), button.getValue()))
      );
    }

    return keyboardBuilder.build();
  }

}
