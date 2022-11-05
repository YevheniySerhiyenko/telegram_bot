package org.expense_bot.util;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.UserRequest;
import org.expense_bot.model.UserSession;
import org.expense_bot.service.impl.UserSessionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSessionUtil {

  private final UserSessionService sessionService;

  public UserSession updateSession(UserRequest userRequest, ConversationState state){
    return UserSession.builder()
	  .state(state)
	  .category("")
	  .build();

  }
}
