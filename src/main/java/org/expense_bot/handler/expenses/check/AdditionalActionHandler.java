package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.util.PDFCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdditionalActionHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;
  private final PDFCreator pdfCreator;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_ADDITIONAL_ACTION);
  }

  @Override
  public void handle(Request request) {
    backButtonHandler.handleExpensesBackButton(request);
    createPDF(request);
    handleInfo(request);
//	if(!hasCallBack(request)) {
//	  final String action = getUpdateData(request);
//	  switch (action){
//		case ExpenseAction.EDIT.name():
//		  handleEdit();
//		  break;
//		default:
//		  throw new IllegalStateException("Unexpected value: " + action);
//	  }
//	}
  }

  private void createPDF(Request request) {
    if(hasMessage(request) && getUpdateData(request).equals(Messages.CREATE_PDF)) {
      final Long chatId = request.getUserId();
      final Session session = sessionService.getSession(chatId);
      final ByteArrayInputStream document = pdfCreator.generatePdf(request, session.getExpenseList());
      telegramService.sendDocument(document, request);
    }
  }

  private String getPeriod(Session session) {
    return Period.valueOf(session.getPeriod()).getValue();
  }

  private void handleInfo(Request request) {
    if(hasCallBack(request) && getUpdateData(request).startsWith(Buttons.BUTTON_INFO)) {
      final Long chatId = request.getUserId();
      final String category = getUpdateData(request).split(" ")[2];
      final Session session = sessionService.getSession(chatId);
      final List<Expense> expenseList = session.getExpenseList();
      final List<Expense> collect = expenseList
        .stream()
        .filter(expense -> StringUtils.startsWithIgnoreCase(expense.getCategory(), category)).collect(Collectors.toList());
//    telegramService.editMessage(request, "");

    }
  }

    @Override
    public boolean isGlobal () {
      return false;
    }

  }
