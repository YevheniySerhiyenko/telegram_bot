package org.expense_bot.util;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.expense_bot.constant.PDFMessages;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class PDFCreator {

  public static final String FONT = "src/fonts/arial.ttf";
  @Value("${pdf.file.size}")
  private Integer FILE_SIZE;


  private final TelegramService telegramService;

  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

  public PDFCreator(
    TelegramService telegramService,
    KeyboardBuilder keyboardBuilder,
    SessionService sessionService,
    ExpenseService expenseService,
    BackButtonHandler backButtonHandler
  ) {
    this.telegramService = telegramService;
    this.keyboardBuilder = keyboardBuilder;
    this.sessionService = sessionService;
    this.expenseService = expenseService;
    this.backButtonHandler = backButtonHandler;
  }

  private static void addTableHeader(PdfPTable table, List<String> strings) {
    strings
      .forEach(columnTitle -> {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.MAGENTA);
        header.setBorderWidth(0.1f);
        header.setPhrase(new Phrase(columnTitle));
        table.addCell(header);
      });
  }


  private static void addRows(PdfPTable table, List<String> column) {
    column.forEach(table::addCell);
  }

  @SneakyThrows
  private static void addCustomRows(PdfPTable table)
    throws URISyntaxException, BadElementException, IOException {
//    Path path = Paths.get(ClassLoader.getSystemResource("/home/yevheniy/Загрузки/153611-samsung_galaxy-samsung-samsung_galaxy_s9-galaxy_s9_po_umolchaniyu-smartfon-1920x1080.jpg").toURI());
//    Image img = Image.getInstance(path.toAbsolutePath().toString());
//    img.scalePercent(10);
//
//    PdfPCell imageCell = new PdfPCell(img);
//    table.addCell(imageCell);

    PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("лосвлствост 2, col 2"));
    horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_TOP);
    table.addCell(horizontalAlignCell);

    PdfPCell verticalAlignCell = new PdfPCell(new Phrase("лосвлствост 2, col 3"));
    verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    table.addCell(verticalAlignCell);
  }

  @SneakyThrows
  public ByteArrayInputStream generatePdf(Request request, List<Expense> expenses) {
    final Long chatId = request.getUserId();
    final Session session = sessionService.getSession(chatId);

    Document document = new Document();
    prepareDocument(document, session);
    document.setPageSize(PageSize.A4.rotate());

    final ByteArrayOutputStream os = new ByteArrayOutputStream(FILE_SIZE);
    PdfWriter.getInstance(document, os);
    final List<String> column = new ArrayList<>();

    final Month month = LocalDate.now().getMonth();
    final int length = month.length(true);
    IntStream.rangeClosed(1, length).forEach(i -> column.add(String.valueOf(i)));

    final List<String> strings = expenses.stream()
      .map(Expense::getCategory)
      .collect(Collectors.toList());
    document.open();
    PdfPTable table = new PdfPTable(expenses.size());
    addheader(table, strings);
//    addTableHeader(table, column);
    addRows(table, column);
    addCustomRows(table);

    document.add(table);

//    Chunk chunk = new Chunk("Hello World", font);
//chunk.setBackground(BaseColor.GREEN);
//    document.add(chunk);
    document.close();
    return new ByteArrayInputStream(os.toByteArray());
  }

  private void prepareDocument(Document document, Session session) throws DocumentException, IOException {
    final BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font font = new Font(bf, 30, Font.NORMAL);

    final Period period = Period.valueOf(session.getPeriod());
    switch (period) {
      case DAY:
        Chunk chunk = new Chunk(String.format(PDFMessages.PERIOD, LocalDate.now()));
        chunk.setFont(font);
        chunk.setBackground(BaseColor.GREEN);
        document.add(chunk);
        break;
      case WEEK:
        break;
      case MONTH:
        break;
      case PERIOD:
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + period);
    }
  }

  private void addheader(PdfPTable table, List<String> strings) throws DocumentException, IOException {
    final BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Stream.of("раз", "два", "авава", "ауака", "лосвлствост", "лосвлствост", "лосвлствост", "лосвлствост")
      .forEach(columnTitle -> {
        Font font = new Font(bf, 30, Font.NORMAL);
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.CYAN);
        header.setBorderWidth(0.1f);
        header.setPhrase(new Phrase(columnTitle, font));
        table.addCell(header);
      });
  }

}
