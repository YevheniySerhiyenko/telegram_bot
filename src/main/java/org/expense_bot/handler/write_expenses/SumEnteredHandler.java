package org.expense_bot.handler.write_expenses;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.Expense;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.TelegramService;
import org.expense_bot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SumEnteredHandler extends UserRequestHandler {

  private static final LocalDateTime NOW = LocalDateTime.now();

  private final TelegramService telegramService;
  private final KeyboardHelper keyboardHelper;
  private final UserSessionService userSessionService;
  private final ExpenseService expenseService;

  private static Expense getSpent(UserSession session) {
	return Expense.builder()
	  .category(session.getCategory())
	  .sum(session.getSum()).dateTime(NOW).build();
  }

  public boolean isApplicable(UserRequest userRequest) {
	return isTextMessage(userRequest.getUpdate()) && ConversationState.WAITING_FOR_SUM.equals(userRequest.getUserSession().getState());
  }

  public void handle(UserRequest userRequest) {
	this.telegramService.sendMessage(userRequest.getChatId(), "Сума витрат записана та відправлена!");
	Double sum = Double.valueOf(userRequest.getUpdate().getMessage().getText());
	UserSession session = userRequest.getUserSession();
	session.setSum(sum);
	session.setState(ConversationState.CONVERSATION_STARTED);
	this.userSessionService.saveSession(userRequest.getChatId(), session);
	this.expenseService.save(getSpent(session));
  }

  public boolean isGlobal() {
	return false;
  }


}
