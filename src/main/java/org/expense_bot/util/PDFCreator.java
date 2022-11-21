package org.expense_bot.util;

import com.itextpdf.html2pdf.HtmlConverter;
import lombok.SneakyThrows;
import org.expense_bot.constant.PDFMessages;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PDFCreator {

  public static final String FONT = "src/fonts/arial.ttf";
  @Value("${pdf.file.size}")
  private Integer FILE_SIZE;

  private final TelegramService telegramService;

  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackHandler backHandler;

  public PDFCreator(
	TelegramService telegramService,
	KeyboardBuilder keyboardBuilder,
	SessionService sessionService,
	ExpenseService expenseService,
	BackHandler backHandler
  ) {
    this.telegramService = telegramService;
    this.keyboardBuilder = keyboardBuilder;
    this.sessionService = sessionService;
    this.expenseService = expenseService;
    this.backHandler = backHandler;
  }

  public static String makePeriodHeader(Session session) {



    final Period period = Period.parsePeriod(session.getPeriod());
    switch (Objects.requireNonNull(period)) {
      case DAY:
        return String.format(PDFMessages.PERIOD, DateUtil.getDate(LocalDateTime.now()));
      case WEEK:
        final String weekRange = DateUtil.getDate(DateUtil.getStartOfWeek()) + "   \t" + DateUtil.getDate(LocalDateTime.now());
        return String.format(PDFMessages.PERIOD, weekRange);
      case MONTH:
        final String monthRange = DateUtil.getDate(DateUtil.getStartOfMonth()) + "   \t" + DateUtil.getDate(LocalDateTime.now());
        return String.format(PDFMessages.PERIOD, monthRange);
      case PERIOD:
        final String periodRange = DateUtil.getDate(session.getPeriodFrom().atStartOfDay()) + "    \t" + DateUtil.getDate(session.getPeriodTo().atStartOfDay());
        return String.format(PDFMessages.PERIOD, periodRange);
      default:
        throw new IllegalStateException("Unexpected value: " + period);
    }
  }

  @SneakyThrows
  public ByteArrayInputStream generatePdf(Request request, List<Expense> expenses) {
    final Long userId = request.getUserId();
    final Session session = sessionService.getSession(userId);
    final List<String> strings = expenses.stream()
      .map(Expense::getCategory)
      .collect(Collectors.toList());

    final ByteArrayOutputStream os = new ByteArrayOutputStream(FILE_SIZE);


    HtmlConverter.convertToDocument((InputStream) null, null);
    return new ByteArrayInputStream(os.toByteArray());
  }

    }

