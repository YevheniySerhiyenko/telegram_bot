package expense_bot.handler.incomes.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Income;
import expense_bot.model.Request;
import expense_bot.service.IncomeService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import expense_bot.util.IncomeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeCheckHandler implements IncomeActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final IncomeService incomeService;

  @Override
  public void handle(Long userId) {
    final List<Income> incomes = incomeService.getAllCurrentMonth(userId);
    incomes.forEach(income -> telegramService.sendMessage(userId, IncomeUtil.getIncome(income)));
    telegramService.sendMessage(userId, Messages.CHOOSE_ANOTHER_PERIOD, keyboardBuilder.buildSetDateMenu());
    sessionService.updateState(userId, ConversationState.Incomes.WAITING_FOR_PERIOD);
  }

  @Override
  public void handleFinal(Request request) {
    // do something
  }

}
