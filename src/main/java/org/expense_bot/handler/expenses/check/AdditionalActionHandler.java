package org.expense_bot.handler.expenses.check;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.Period;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.handler.init.BackButtonHandler;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.expense_bot.util.PDFCreator;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class AdditionalActionHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;
  private final BackButtonHandler backButtonHandler;
  private final PDFCreator pdfCreator;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isEqual(request, ConversationState.Expenses.WAITING_ADDITIONAL_ACTION);
  }

  @Override
  public void handle(UserRequest request) {
    backButtonHandler.handleExpensesBackButton(request);
    createPDF(request);
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

  private void createPDF(UserRequest request) {
    if(getUpdateData(request).equals(Messages.CREATE_PDF)) {
      final Long chatId = request.getChatId();
      final UserSession session = userSessionService.getSession(chatId);
      final ByteArrayInputStream document = pdfCreator.generatePdf(request, session.getExpenseList());
      telegramService.sendDocument(document, request);
    }
  }

  private String getPeriod(UserSession session) {
    return Period.valueOf(session.getPeriod()).getValue();
  }

  private void handleEdit() {

  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
