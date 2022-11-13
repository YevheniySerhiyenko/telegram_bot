package org.expense_bot.util;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
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
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.expense_bot.util.PDFCreator.FONT;

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
    final List<String> strings = expenses.stream()
      .map(Expense::getCategory)
      .collect(Collectors.toList());
//
    Document document = new Document();
    final ByteArrayOutputStream os = new ByteArrayOutputStream(FILE_SIZE);
    PdfWriter.getInstance(document, os);
    document.setPageSize(PageSize.A4.rotate());
    document.open();
    FirstPdf.addMetaData(document,session);
    FirstPdf.addTitlePage(document,session);
    FirstPdf.addContent(document,strings);
    document.close();
    return new ByteArrayInputStream(os.toByteArray());
  }

//    Document document = new Document();
//    document.setPageSize(PageSize.A4.rotate());
//
//    final ByteArrayOutputStream os = new ByteArrayOutputStream(FILE_SIZE);
//    PdfWriter.getInstance(document, os);
//
//    final Month month = LocalDate.now().getMonth();
//    final int length = month.length(true);
//
//    final List<String> strings = expenses.stream()
//      .map(Expense::getCategory)
//      .collect(Collectors.toList());
//
//    document.open();
//    PdfPTable table = new PdfPTable(7);
//    table.setHorizontalAlignment(Element.ALIGN_CENTER);
//    table.setHeaderRows(34);
////    final Chunk chunk = prepareDocument(session);
//    final Paragraph paragraph = new Paragraph();
//    paragraph.setAlignment(Element.ALIGN_CENTER);
//    paragraph.setSpacingAfter(30);
//    document.add(paragraph);
////
//    document.addTitle(prepareDocument(session));
//
//    document.setMargins(25,65,2,23);
//    Paragraph tabl = new Paragraph();
//    paragraph.add(table);
//    paragraph.setIndentationLeft(Element.ALIGN_CENTER);
//    paragraph.setAlignment(Element.ALIGN_CENTER);
//    paragraph.setIndentationRight(Element.ALIGN_CENTER);
//    document.add(tabl);
//    addheader(table, session.getPeriod());
////    addTableHeader(table, column);
//    addRows(table, strings);
//    addCustomRows(table);
//
//    document.add(table);
//
//    Chunk chunk = new Chunk("Hello World");
//chunk.setBackground(BaseColor.GREEN);
//    document.add(chunk);
//    document.close();


  public static String prepareDocument(Session session) throws DocumentException, IOException {
    final BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font font = new Font(bf, 40, Font.BOLD);

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

  private void addheader(PdfPTable table, String period) throws DocumentException, IOException {
    final Period period1 = Period.parsePeriod(period);
    List<String> strings = new ArrayList<>();
    switch (Objects.requireNonNull(period1)) {
      case WEEK:
        final int dayOfMonth = DateUtil.getStartOfWeek().getDayOfMonth();
        final int dayOfMonth1 = LocalDate.now().getDayOfMonth();

        IntStream.rangeClosed(dayOfMonth, dayOfMonth1).forEach(i -> strings.add(String.valueOf(i)));
    }
    final BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    strings.forEach(columnTitle -> {
      Font font = new Font(bf, 14, Font.NORMAL);
      PdfPCell header = new PdfPCell();
      header.setBackgroundColor(BaseColor.CYAN);
      header.setBorderWidth(0.1f);
      header.setVerticalAlignment(Element.ALIGN_CENTER);
      header.setHorizontalAlignment(Element.ALIGN_CENTER);
      header.setPhrase(new Phrase(columnTitle, font));
      table.addCell(header);
    });
  }
}

   class FirstPdf {
    private static String FILE = "c:/temp/FirstPdf.pdf";

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 40,
      Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
      Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.BOLD);

     FirstPdf() throws DocumentException, IOException {
     }


     // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    static void addMetaData(Document document, Session session) throws DocumentException, IOException {
      document.addTitle(PDFCreator.prepareDocument(session));
      document.addSubject("Using iText");
      document.addKeywords("Java, PDF, iText");
      document.addAuthor("Lars Vogel");
      document.addCreator("Lars Vogel");
    }

    static void addTitlePage(Document document, Session session)
      throws DocumentException, IOException {
      Paragraph preface = new Paragraph();
      // We add one empty line
//      addEmptyLine(preface, 1);
      // Lets write a big header
       BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
       Font font = new Font(bf, 40, Font.BOLD);
      preface.add(new Paragraph(PDFCreator.prepareDocument(session), font));

      addEmptyLine(preface, 1);
      // Will create: Report generated by: _name, _date
      preface.add(new Paragraph(
        "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        smallBold));
      addEmptyLine(preface, 3);
      preface.add(new Paragraph(
        "This document describes something which is very important ",
        smallBold));

      addEmptyLine(preface, 8);

      preface.add(new Paragraph(
        "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
        redFont));

      document.add(preface);
      // Start a new page
      document.newPage();
    }

    static void addContent(Document document, List<String> expenses) throws DocumentException, IOException {
      Anchor anchor = new Anchor("First Chapter", catFont);
      anchor.setName("First Chapter");

      // Second parameter is the number of the chapter
      Chapter catPart = new Chapter(new Paragraph(anchor), 1);

      Paragraph subPara = new Paragraph("Subcategory 1", subFont);
      Section subCatPart = catPart.addSection(subPara);
      subCatPart.add(new Paragraph("Hello"));

      subPara = new Paragraph("Subcategory 2", subFont);
      subCatPart = catPart.addSection(subPara);
      subCatPart.add(new Paragraph("Paragraph 1"));
      subCatPart.add(new Paragraph("Paragraph 2"));
      subCatPart.add(new Paragraph("Paragraph 3"));

      // add a list
//      createList(subCatPart);
      Paragraph paragraph = new Paragraph();
      addEmptyLine(paragraph, 5);
      subCatPart.add(paragraph);

      // add a table
      createTable(subCatPart, expenses);

      // now add all this to the document
      document.add(catPart);

      // Next section
      anchor = new Anchor("Second Chapter", catFont);
      anchor.setName("Second Chapter");

      // Second parameter is the number of the chapter
      catPart = new Chapter(new Paragraph(anchor), 1);

      subPara = new Paragraph("Subcategory", subFont);
      subCatPart = catPart.addSection(subPara);
      subCatPart.add(new Paragraph("This is a very important message"));

      // now add all this to the document
      document.add(catPart);

    }

    private static void createTable(Section subCatPart, List<String> expenses)
      throws DocumentException, IOException {
      PdfPTable table = new PdfPTable(expenses.size());

      // t.setBorderColor(BaseColor.GRAY);
      // t.setPadding(4);
      // t.setSpacing(4);
      // t.setBorderWidth(1);
      BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

      PdfPCell c1 = new PdfPCell(new Phrase());
      c1.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(c1);
      Font font = new Font(bf, 13, Font.BOLD);
expenses.forEach(i -> {

    PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(i),font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setVerticalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);

//      cell = new PdfPCell(new Phrase(i,font));
//      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//      table.addCell(cell);
//
//      cell = new PdfPCell(new Phrase(i,font));
//      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//      table.addCell(cell);
});
      table.setHeaderRows(1);

      table.addCell("1.0");
      table.addCell("1.1");
      table.addCell("1.2");
      table.addCell("2.1");
      table.addCell("2.2");
      table.addCell("2.3");

      subCatPart.add(table);

    }

//    private static void createList(Section subCatPart) {
//      List list = new List(true, false, 10);
//      list.add(new ListItem("First point"));
//      list.add(new ListItem("Second point"));
//      list.add(new ListItem("Third point"));
//      subCatPart.add(list);
//    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
      for (int i = 0; i < number; i++) {
        paragraph.add(new Paragraph(" "));
      }
    }
  }

