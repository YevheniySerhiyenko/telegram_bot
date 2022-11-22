package org.expense_bot.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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


    final Map<String, Object> objectMap = new HashMap<>() {
      {
        put("periodFrom", session.getPeriodFrom());
        put("periodTo", session.getPeriodTo());
        put("userId", session.getUserId());
        put("state", session.getState());
        put("categoryAction", session.getCategoryAction());
        put("action", session.getAction());
        put("category", session.getCategory());
        put("expenseSum", session.getExpenseSum());
        put("incomeSum", session.getIncomeSum());
        put("expenseDate", session.getExpenseDate());
        put("incomeDate", session.getIncomeDate());
        put("incomeAction", session.getIncomeAction());
        put("period", session.getPeriod());
        put("stickerAction", session.getStickerAction());
        put("expenseList", session.getExpenseList());
        put("incomeList", "session.getIncomeList()");

      }
    };

    return generatePdfFile(objectMap);
  }


  @Transactional
  public ByteArrayInputStream generatePdfFile(Map<String, Object> data) {
    final Context context = new Context();
    context.setVariables(data);

    TemplateEngine templateEngine = new TemplateEngine();
    final String s = "thymeleaf_template.html";
    final String htmlContent = templateEngine.process(s, context);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final ConverterProperties converterProperties = new ConverterProperties();
    final FontProvider fontProvider = new DefaultFontProvider();
//    fontProvider.addFont(regularFont);
//    fontProvider.addFont(boldFont);
    converterProperties.setFontProvider(fontProvider);
    HtmlConverter.convertToPdf(htmlContent, outputStream,converterProperties);

    return new ByteArrayInputStream(outputStream.toByteArray());
  }
}

