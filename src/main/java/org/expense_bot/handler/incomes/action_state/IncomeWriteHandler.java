package org.expense_bot.handler.incomes.action_state;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.exception.InvalidAmountException;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Request;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.IncomeService;
import org.expense_bot.service.impl.SessionService;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.util.IncomeUtil;
import org.expense_bot.util.SessionUtil;
import org.expense_bot.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	sessionService.update(SessionUtil.getSession(userId));
  }

  @Override
  public void handleFinal(Request request) {
	final Long userId = request.getUserId();
	final Optional<BigDecimal> sum = getSum(request);
	if(sum.isEmpty()) {
	  return;
	}
	final LocalDate incomeDate = request.getSession().getIncomeDate();
	incomeService.save(IncomeUtil.buildIncome(userId, sum.get(), incomeDate));
	stickerSender.sendSticker(userId, StickerAction.SUCCESS_EXPENSE_SUM.name());
	telegramService.sendMessage(userId, Messages.SUCCESS_INCOME, keyboardBuilder.buildIncomeMenu());
	sessionService.updateState(userId, ConversationState.Init.WAITING_INCOME_ACTION);
  }

  @ExceptionHandler(InvalidAmountException.class)
  private Optional<BigDecimal> getSum(Request request) {
	try {
	  return Utils.checkWrongSum(request);
	} catch (NumberFormatException e) {
	  final Long userId = request.getUserId();
	  stickerSender.sendSticker(userId, StickerAction.WRONG_ENTERED_SUM.name());
	  telegramService.sendMessage(userId, Messages.WRONG_SUM_FORMAT);
	}
	return Optional.empty();
  }

}
