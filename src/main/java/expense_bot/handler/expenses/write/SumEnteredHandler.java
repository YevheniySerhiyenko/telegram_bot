package expense_bot.handler.expenses.write;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.handler.RequestHandler;
import expense_bot.handler.init.BackHandler;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Request;
import expense_bot.service.ExpenseService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.ExpenseUtil;
import expense_bot.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SumEnteredHandler extends RequestHandler {

  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final SessionService sessionService;
  private final ExpenseService expenseService;
  private final BackHandler backHandler;


  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Expenses.WAITING_FOR_EXPENSE_SUM);
  }

  @Override
  public void handle(Request request) {
    if (backHandler.handleExpensesBackButton(request)) {
      return;
    }
    final boolean enteredDate = sessionService.checkEnteredDate(
      request, ConversationState.Expenses.WAITING_FOR_ANOTHER_EXPENSE_DATE);
    if (enteredDate) {
      return;
    }
    if (hasMessage(request)) {
      final Optional<BigDecimal> sum = sessionService.getSum(request);
      if (sum.isEmpty()) {
        return;
      }
      final Long userId = request.getUserId();
      final ReplyKeyboard keyboard = keyboardBuilder.buildExpenseMenu();
      sessionService.update(SessionUtil.getSession(sum.get(), userId));
      expenseService.save(ExpenseUtil.getExpense(sessionService.get(userId)));
      telegramService.sendMessage(userId, Messages.SUCCESS);
      telegramService.sendMessage(userId, Messages.SUCCESS_SENT_SUM, keyboard);
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

}
