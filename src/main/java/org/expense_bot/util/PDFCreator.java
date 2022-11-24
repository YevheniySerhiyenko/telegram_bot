package org.expense_bot.util;

import com.itextpdf.html2pdf.HtmlConverter;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PDFCreator {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackHandler backHandler;

  @Autowired
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

  public ByteArrayInputStream generatePdf(Request request, List<Expense> expenses) {
	final Long userId = request.getUserId();
	final Session session = sessionService.getSession(userId);


	final String period = session.getPeriod();
	final LocalDate periodFrom = session.getPeriodFrom();
	final LocalDate periodTo = session.getPeriodTo();
	final Map<String, Object> objectMap = new HashMap<>() {
	  {
		put("expenses", expenses);
		put("period", period);
		put("periodFrom", periodFrom);
		put("periodTo", periodTo);
	  }
	};

	final Context context = new Context();
	context.setVariables(objectMap);

	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
	templateResolver.setSuffix(".html");
	templateResolver.setTemplateMode(TemplateMode.HTML);

	TemplateEngine templateEngine = new TemplateEngine();
	templateEngine.setTemplateResolver(templateResolver);
	final String s = "expenses.html";
	final String htmlContent = templateEngine.process(s, context);

	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    final ConverterProperties converterProperties = new ConverterProperties();
//    final FontProvider fontProvider = new DefaultFontProvider();
//    fontProvider.addFont(regularFont);
//    fontProvider.addFont(boldFont);
//    converterProperties.setFontProvider(fontProvider);
	HtmlConverter.convertToPdf(htmlContent, outputStream);

	return new ByteArrayInputStream(outputStream.toByteArray());
  }

}
