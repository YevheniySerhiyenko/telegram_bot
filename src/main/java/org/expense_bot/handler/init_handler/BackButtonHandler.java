package org.expense_bot.handler.init_handler;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.handler.UserRequestHandler;
import org.expense_bot.helper.KeyboardHelper;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.TelegramService;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

import static org.expense_bot.constant.Constants.BUTTON_BACK;

@Component
@RequiredArgsConstructor
public class BackButtonHandler extends UserRequestHandler {

  private final TelegramService telegramService;
  private final UserSessionService userSessionService;
  private final KeyboardHelper keyboardHelper;


  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextMessage(request.getUpdate(), BUTTON_BACK);
  }

  @Override
  public void handle(UserRequest request) {
    final Long chatId = request.getChatId();
    final UserSession lastSession = userSessionService.getLastSession(chatId);
    final ConversationState lastSessionState = lastSession.getState();
    if(lastSessionState != null){
      final UserSession userSession = UserSession.builder()
        .chatId(chatId)
        .state(lastSessionState)
        .build();
      userSessionService.saveSession(chatId,userSession);
    }

  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
