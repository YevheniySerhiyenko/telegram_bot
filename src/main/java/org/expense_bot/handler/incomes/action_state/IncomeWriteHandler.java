package org.expense_bot.handler.incomes.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.IncomeAction;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.IncomeUtil;
import org.expense_bot.util.SessionUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncomeWriteHandler implements IncomeActionState {

  private final SessionService sessionService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final IncomeService incomeService;
  private final StickerSender stickerSender;

  @Override
  public void handle(Long userId) {
	telegramService.sendMessage(userId, Messages.ENTER_INCOME_SUM, keyboardBuilder.buildSetDateMenu());
	sessionService.update(SessionUtil.getSession(IncomeAction.WRITE, userId));
  }

  @Override
  public void handleFinal(Request request) {
	final Long userId = request.getUserId();
	final Optional<BigDecimal> sum = sessionService.getSum(request);
	if(sum.isEmpty()) {
	  return;
	}
	final LocalDate incomeDate = request.getSession().getIncomeDate();
	incomeService.save(IncomeUtil.buildIncome(userId, sum.get(), incomeDate));
	stickerSender.sendSticker(userId, StickerAction.SUCCESS_EXPENSE_SUM.name());
	telegramService.sendMessage(userId, Messages.SUCCESS_INCOME, keyboardBuilder.buildIncomeMenu());
	sessionService.updateState(userId, ConversationState.Init.WAITING_INCOME_ACTION);
  }

}
