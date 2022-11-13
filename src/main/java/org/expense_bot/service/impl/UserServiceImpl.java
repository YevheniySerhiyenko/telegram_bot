package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.handler.NewUserHandler;
import org.expense_bot.model.User;
import org.expense_bot.model.Request;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.service.StickerService;
import org.expense_bot.service.UserService;
import org.expense_bot.service.UserStickerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TelegramService telegramService;
  private final StickerService stickerService;
  private final UserStickerService userStickerService;
  private final NewUserHandler firstEnteredHandler;

  @Override
  public void checkUser(Request userRequest) {
	final String firstName = getFirstName(userRequest);
	final Optional<User> user = getByChatId(userRequest.getUserId());
	if(!user.isPresent()){
	  firstEnteredHandler.handle(userRequest);
	}

	 userRepository.save(getUser(userRequest, firstName));
  }

  private String getFirstName(Request userRequest) {
    if(userRequest.getUpdate().hasMessage()){
	  return userRequest.getUpdate().getMessage().getFrom().getFirstName();
	}
    return userRequest.getUpdate().getCallbackQuery().getFrom().getFirstName();
  }

  private Optional<User> getByChatId(Long chatId) {
	return userRepository.findByChatId(chatId);
  }

  private User getUser(Request request, String firstName) {
	return User.builder()
	  .name(firstName)
	  .chatId(request.getUserId())
	  .build();
  }

}
