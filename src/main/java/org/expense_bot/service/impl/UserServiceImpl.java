package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.User;
import org.expense_bot.model.UserRequest;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TelegramService telegramService;

  @Override
  public User checkUser(UserRequest userRequest) {
	final String firstName = userRequest.getUpdate().getMessage().getFrom().getFirstName();
	final Optional<User> user = getByChatId(userRequest.getChatId());
	if(user.isEmpty()){
	  firstEnterHandle(userRequest);
	}

	return user.orElse(userRepository.save(getUser(userRequest, firstName)));
  }

  private void firstEnterHandle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
	telegramService.sendMessage(chatId, Messages.HELLO);
  }

  @Override
  public Optional<User> getByChatId(Long chatId) {
	return userRepository.findByChatId(chatId);
  }

  private User getUser(UserRequest request, String firstName) {
	return User.builder()
	  .name(firstName)
	  .chatId(request.getChatId())
	  .build();
  }

}
