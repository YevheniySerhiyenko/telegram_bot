package org.expense_bot;

import lombok.extern.slf4j.Slf4j;
import org.expense_bot.model.Request;
import org.expense_bot.model.Session;
import org.expense_bot.service.impl.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class ExpenseBot extends TelegramLongPollingBot {

  /**
   *
   */
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    private final Dispatcher dispatcher;
    private final SessionService sessionService;

    public ExpenseBot(Dispatcher dispatcher, SessionService sessionService) {
        this.dispatcher = dispatcher;
        this.sessionService = sessionService;
    }

    /**
     * This is an entry point for any messages, or updates received from user<br>
     * Docs for "Update object: https://core.telegram.org/bots/api#update
     */
    @Override
    public void onUpdateReceived(Update update) {
      final String textFromUser = getText(update);
      final Long userId = getUserId(update);
      String userFirstName = getFirstName(update);

      log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);

      Session session = sessionService.getSession(userId);
      Request request = Request
        .builder()
        .update(update)
        .session(session)
        .userId(userId)
        .build();

      boolean dispatched = dispatcher.dispatch(request);

      if(!dispatched) {
        log.warn("Unexpected update from user");
      }
    }

  private String getText(Update update) {
      if(update.hasMessage()){
        return update.getMessage().getText();
      }
      return update.getCallbackQuery().getData();
  }

  private String getFirstName(Update update) {
    if(update.hasMessage()) {
      return update.getMessage().getFrom().getFirstName();
    }
    return update.getCallbackQuery().getFrom().getFirstName();
  }

  private Long getUserId(Update update) {
    if(update.hasMessage()) {
      return update.getMessage().getChatId();
    }
    return update.getCallbackQuery().getFrom().getId();
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