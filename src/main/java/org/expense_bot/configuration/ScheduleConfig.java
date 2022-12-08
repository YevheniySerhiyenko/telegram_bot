package org.expense_bot.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.enums.Button;
import org.expense_bot.enums.Period;
import org.expense_bot.helper.KeyboardBuilder;
import org.expense_bot.model.Expense;
import org.expense_bot.service.ExpenseService;
import org.expense_bot.service.UserService;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {

  private final UserService userService;
  private final TelegramService telegramService;
  private final KeyboardBuilder keyboardBuilder;
  private final ExpenseService expenseService;

  @Scheduled(cron = "0 0 21 * * *")
  public void sendNotifications() {

	userService.getAll().forEach(user -> {
	  final LocalDateTime dateFrom = Period.DAY.getDateFrom();
	  final LocalDateTime dateTo = Period.DAY.getDateTo();
	  final String byAllCategories = Button.BY_ALL_CATEGORIES.getValue();
	  final List<Expense> byPeriod = expenseService.getByPeriod(user.getUserId(), dateFrom, dateTo, byAllCategories);
	  if(byPeriod.isEmpty()) {
		telegramService.sendMessage(
		  user.getUserId(), "Hello, write your expenses", keyboardBuilder.buildExpenseMenu());
	  }
	});
  }

  @Scheduled(cron = "*/30 * * * * *")
  public void closeUserSessions(){
    log.info("Scheduler for close session started");
    userService.getAll().forEach(user -> userService.closeSession(user.getUserId()));
  }

}
