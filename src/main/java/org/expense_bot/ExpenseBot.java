package org.expense_bot;

import lombok.extern.slf4j.Slf4j;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class ExpenseBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    private final Dispatcher dispatcher;
    private final UserSessionService userSessionService;

    public ExpenseBot(Dispatcher dispatcher, UserSessionService userSessionService) {
        this.dispatcher = dispatcher;
        this.userSessionService = userSessionService;
    }

    /**
     * This is an entry point for any messages, or updates received from user<br>
     * Docs for "Update object: https://core.telegram.org/bots/api#update
     */
    @Override
    public void onUpdateReceived(Update update) {
      final String textFromUser = getText(update);
      final Long chatId = getChatId(update);
      String userFirstName = getFirstName(update);

      log.info("[{}, {}] : {}", chatId, userFirstName, textFromUser);

      UserSession session = userSessionService.getSession(chatId);
      UserRequest userRequest = UserRequest
        .builder()
        .update(update)
        .userSession(session)
        .chatId(chatId)
        .build();

      boolean dispatched = dispatcher.dispatch(userRequest);

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

  private Long getChatId(Update update) {
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