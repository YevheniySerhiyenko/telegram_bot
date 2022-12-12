package expense_bot.handler.expenses.check;

import expense_bot.enums.Button;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.mapper.ExpenseMapper;
import expense_bot.model.Expense;
import expense_bot.model.Request;
import expense_bot.model.Session;
import expense_bot.service.ExpenseService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.PDFCreator;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FinalActionHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackHandler backHandler;
  private final PDFCreator pdfCreator;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_ADDITIONAL_ACTION);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    createPDF(request);
    handleInfo(request);
    sessionService.update(SessionUtil.getSession(request.getUserId()));
  }

  private void createPDF(Request request) {
    if (hasMessage(request) && getUpdateData(request).equals(Button.CREATE_PDF.getValue())) {
      final Long userId = request.getUserId();
      final Session session = sessionService.get(userId);
      final ByteArrayInputStream document = pdfCreator.generatePdf(request, session.getExpenseList());
      telegramService.sendDocument(document, request);
    }
  }

  private void handleInfo(Request request) {
    if (hasCallBack(request)) {
      final Long userId = request.getUserId();
      final String category = getUpdateData(request);
      final Session session = sessionService.get(userId);
      final List<Expense> expenseList = session.getExpenseList();
      final List<Expense> expenses = expenseList
        .stream()
        .filter(expense -> StringUtils.startsWithIgnoreCase(expense.getCategory(), category))
        .collect(Collectors.toList());
      final String text = request.getUpdate().getCallbackQuery().getMessage().getText();
      final List<String> list = new ArrayList<>(List.of(text));
      list.addAll(ExpenseMapper.toDetailExpense(expenses));
      telegramService.buildDetailMessage(request, list);
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
