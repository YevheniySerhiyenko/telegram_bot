package expense_bot.util;

import expense_bot.model.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static expense_bot.handler.RequestHandler.getUpdateData;
import static expense_bot.handler.RequestHandler.hasCallBack;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Calendar {

  public static final String BACK = "back";
  private static final String PATTERN = "dd.MM.yyyy";
  private static final String FORWARD = "forward";
  private static final String BLANK = " ";

  public static InlineKeyboardMarkup buildCalendar(LocalDate now) {
    final Month month = now.getMonth();
    final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
    final int numberOfDays = month.length(true);
    keyboard.setKeyboard(
      List.of(
        getDateLine(month.name(), now.getYear()),
        getNumbersLine(1, 8, now),
        getNumbersLine(9, 16, now),
        getNumbersLine(17, 24, now),
        getNumbersLine(25, numberOfDays, now),
        getLastLine(now))
    );
    return keyboard;
  }

  private static List<InlineKeyboardButton> getLastLine(LocalDate now) {
    return List.of(
      Utils.buildButton("<", BACK + BLANK + now.getMonth() + BLANK + now.getYear()),
      Utils.buildButton(">", FORWARD + BLANK + now.getMonth() + BLANK + now.getYear()));
  }

  private static List<InlineKeyboardButton> getDateLine(String month, Integer year) {
    final String date = month + BLANK + year;
    return Collections.singletonList(Utils.buildButton(date, date));
  }

  private static List<InlineKeyboardButton> getNumbersLine(int number, int numberOfDays, LocalDate now) {
    return IntStream.rangeClosed(number, numberOfDays)
      .mapToObj(num -> Utils.buildButton(String.valueOf(num), getCallBackData(now, num)))
      .collect(Collectors.toList());
  }

  private static String getCallBackData(LocalDate now, int number) {
    final String callBackDayValue = getCallBackFormatValue(String.valueOf(number));
    final String callBackMonthValue = getCallBackFormatValue(String.valueOf(now.getMonth().getValue()));
    return callBackDayValue + "." + callBackMonthValue + "." + now.getYear();
  }

  private static String getCallBackFormatValue(String value) {
    return value.length() == 1 ? "0" + value : value;
  }

  public static Optional<InlineKeyboardMarkup> changeMonth(Request request) {
    if (hasCallBack(request)) {
      final String data = getUpdateData(request);
      if (data.startsWith(BACK) || data.startsWith(FORWARD)) {
        final String[] text = data.split(BLANK);
        final String command = text[0];
        final LocalDate of = getNextOrPreviousMonth(text);

        switch (command) {
          case BACK:
            return Optional.of(Calendar.buildCalendar(of.minusMonths(1)));
          case FORWARD:
            return Optional.of(Calendar.buildCalendar(of.plusMonths(1)));
          default:
            return Optional.empty();
        }
      }
    }
    return Optional.empty();
  }

  public static Optional<InlineKeyboardMarkup> changeYear(Request request) {
    if (hasCallBack(request)) {
      final String data = getUpdateData(request);
      if (data.startsWith(BACK) || data.startsWith(FORWARD)) {
        final String[] text = data.split(BLANK);
        final String command = text[0];
        final LocalDate of = getNextOrPreviousYear(text);

        if (BACK.equals(command)) {
          return Optional.of(Calendar.buildYear(of.minusYears(1)));
        } else if (FORWARD.equals(command)) {
          return Optional.of(Calendar.buildYear(of.plusYears(1)));
        }
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  private static LocalDate getNextOrPreviousYear(String[] text) {
    final Month month = Month.valueOf(text[1]);
    final int year = Integer.parseInt(text[2]);
    return LocalDate.of(year, month, 1);
  }

  private static LocalDate getNextOrPreviousMonth(String[] text) {
    return LocalDate.of(LocalDate.now().getYear(), Month.valueOf(text[1]), 1);
  }

  public static LocalDate getDate(Request request) {
    if (hasCallBack(request)) {
      try {
        return LocalDate.parse(getUpdateData(request), DateTimeFormatter.ofPattern(PATTERN));
      } catch (DateTimeParseException ignored) {
        log.warn("Selected header in calendar");
      }
    }
    return LocalDate.now();
  }

  public static InlineKeyboardMarkup buildYear(LocalDate date) {
    final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
    final int year = date.getYear();

    keyboardMarkup.setKeyboard(
      List.of(
        getDateLine(BLANK, year),
        getMonths(0, 2, year),
        getMonths(3, 5, year),
        getMonths(6, 8, year),
        getMonths(9, 11, year),
        getLastLine(date)
      ));
    return keyboardMarkup;
  }

  private static List<InlineKeyboardButton> getMonths(Integer startMonth, Integer lastMonth, Integer year) {
    final Month[] values = Month.values();
    return IntStream.rangeClosed(startMonth, lastMonth)
      .mapToObj(i -> String.valueOf(values[i]))
      .map(data -> Utils.buildButton(data, data + BLANK + year))
      .collect(Collectors.toList());
  }

  public static LocalDate parseMonthYear(String date) {
    final String[] monthYear = date.split(BLANK);
    final Month month = Month.valueOf(monthYear[0]);
    final int year = Integer.parseInt(monthYear[1]);
    return LocalDate.of(year, month, 1);
  }

}
