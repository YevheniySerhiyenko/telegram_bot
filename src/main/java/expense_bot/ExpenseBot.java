package expense_bot;

import expense_bot.handler.init.StartCommandHandler;
import expense_bot.model.Request;
import expense_bot.model.Session;
import expense_bot.service.impl.SessionService;
import expense_bot.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class ExpenseBot extends TelegramLongPollingBot {

  private final Dispatcher dispatcher;
  private final SessionService sessionService;
  private final StartCommandHandler startCommandHandler;

  @Value("${bot.token}")
  private String botToken;
  @Value("${bot.username}")
  private String botUsername;

  public ExpenseBot(Dispatcher dispatcher, SessionService sessionService, StartCommandHandler startCommandHandler) {
    this.dispatcher = dispatcher;
    this.sessionService = sessionService;
    this.startCommandHandler = startCommandHandler;
  }

  /**
   * This is an entry point for any messages, or updates received from user<br>
   * Docs for "Update object: https://core.telegram.org/bots/api#update
   */
  @Override
  public void onUpdateReceived(Update update) {
    final String textFromUser = Utils.getText(update);
    final Long userId = Utils.getUserId(update);
    final String userFirstName = Utils.getFirstName(update);

    log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);

    final Session session = sessionService.get(userId);
    final Request request = getRequest(update, userId, session);

    try {
      if (!dispatcher.dispatch(request)) {
        log.warn("Unexpected update from user");
      }
    } catch (Exception e) {
      log.error("Error", e);
      startCommandHandler.handle(request);
    }
  }

  private Request getRequest(Update update, Long userId, Session session) {
    return Request
      .builder()
      .update(update)
      .session(session)
      .userId(userId)
      .build();
  }

  @Override
  public String getBotUsername() {
    // username which you give to your bot bia BotFather (without @)
    return botUsername;
  }

  @Override
  public String getBotToken() {
    // do not expose the token to the repository,
    // always provide it externally(for example as environmental variable)
    return botToken;
  }

}