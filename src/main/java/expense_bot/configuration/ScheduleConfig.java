package expense_bot.configuration;

import expense_bot.enums.Button;
import expense_bot.enums.Period;
import expense_bot.enums.StickerAction;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.model.Expense;
import expense_bot.sender.StickerSender;
import expense_bot.service.ExpenseService;
import expense_bot.service.UserService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final ExpenseService expenseService;
  private final StickerSender stickerSender;

  @Scheduled(cron = "0 0 21 * * *")
  public void sendNotifications() {

    userService.getAll().forEach(user -> {
      final LocalDateTime dateFrom = Period.DAY.getDateFrom();
      final LocalDateTime dateTo = Period.DAY.getDateTo();
      final String byAllCategories = Button.BY_ALL_CATEGORIES.getValue();
      final List<Expense> byPeriod = expenseService.getByPeriod(user.getUserId(), dateFrom, dateTo, byAllCategories);
      if (byPeriod.isEmpty()) {
        telegramService.sendMessage(
          user.getUserId(), "Hello, write your expenses", KeyboardBuilder.buildExpenseMenu());
        stickerSender.sendSticker(user.getUserId(), StickerAction.NOTIFICATION.name());
      }
    });
  }

  @Scheduled(cron = "*/30 * * * * *")
  public void closeUserSessions() {
    userService.getAll().forEach(user -> userService.closeSession(user.getUserId()));
  }

}
