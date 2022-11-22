package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.Button;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.mapper.ExpenseMapper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.PDFCreator;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdditionalActionHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
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
    if(backHandler.handleExpensesBackButton(request)) {
      return;
    }
    createPDF(request);
    handleInfo(request);
    sessionService.update(SessionUtil.finalUpdate(request.getUserId()));
  }

  private void createPDF(Request request) {
    if(hasMessage(request) && getUpdateData(request).equals(Button.CREATE_PDF.getValue())) {
      final Long userId = request.getUserId();
      final Session session = sessionService.getSession(userId);
      final ByteArrayInputStream document = pdfCreator.generatePdf(request, session.getExpenseList());
      telegramService.sendDocument(document, request);
    }
  }

  private void handleInfo(Request request) {
    if(hasCallBack(request)) {
      final Long userId = request.getUserId();
      final String category = getUpdateData(request);
      final Session session = sessionService.getSession(userId);
      final List<Expense> expenseList = session.getExpenseList();
      final List<Expense> collect = expenseList
        .stream()
        .filter(expense -> StringUtils.startsWithIgnoreCase(expense.getCategory(), category))
        .collect(Collectors.toList());
      final String text = request.getUpdate().getCallbackQuery().getMessage().getText();
      final List<String> list = new ArrayList<>(List.of(text));
      list.addAll(ExpenseMapper.toDetailExpense(collect));
      telegramService.buildDetailMessage(request, list);
    }
  }

    @Override
    public boolean isGlobal () {
      return false;
    }

  }
