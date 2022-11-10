package org.expense_bot.util;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class PDFCreator {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;

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

  private static void addCustomRows(PdfPTable table)
    throws URISyntaxException, BadElementException, IOException {
//    Path path = Paths.get(ClassLoader.getSystemResource("/home/yevheniy/Загрузки/153611-samsung_galaxy-samsung-samsung_galaxy_s9-galaxy_s9_po_umolchaniyu-smartfon-1920x1080.jpg").toURI());
//    Image img = Image.getInstance(path.toAbsolutePath().toString());
//    img.scalePercent(10);
//
//    PdfPCell imageCell = new PdfPCell(img);
//    table.addCell(imageCell);

    PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
    horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(horizontalAlignCell);

    PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
    verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    table.addCell(verticalAlignCell);
  }

  @SneakyThrows
  public ByteArrayInputStream generatePdf(UserRequest request, List<Expense> expenses) {
    final Long chatId = request.getChatId();
    final UserSession session = userSessionService.getSession(chatId);

    Document document = new Document();
    document.setPageSize(PageSize.A4.rotate());

    //4096 to properties
    final ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
    PdfWriter.getInstance(document, os);
    final List<String> column = new ArrayList<>();

    Font font = FontFactory.getFont(FontFactory.COURIER, 54, BaseColor.BLACK);
    final Month month = LocalDate.now().getMonth();
    final int length = month.length(true);
    IntStream.rangeClosed(1, length).forEach(i -> column.add(String.valueOf(i)));

    final List<String> strings = expenses.stream().map(Expense::getCategory).collect(Collectors.toList());
    document.open();
    PdfPTable table = new PdfPTable(expenses.size());
    addheader(table,strings);
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

  private void addheader(PdfPTable table, List<String> strings) {
    Stream.of("раз","два","row","row","row","row","row","row")
      .forEach(columnTitle -> {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.MAGENTA);
        header.setBorderWidth(0.1f);
        header.setPhrase(new Phrase(columnTitle));
        table.addCell(header);
      });
  }

}
