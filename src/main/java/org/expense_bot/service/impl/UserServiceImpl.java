package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.User;
import org.expense_bot.model.UserRequest;
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

  @Override
  public User checkUser(UserRequest userRequest) {
	final String firstName = getFirstName(userRequest);
	final Optional<User> user = getByChatId(userRequest.getChatId());
	if(!user.isPresent()){
	  firstEnterHandle(userRequest);
	}

	return user.orElse(userRepository.save(getUser(userRequest, firstName)));
  }

  private String getFirstName(UserRequest userRequest) {
    if(userRequest.getUpdate().hasMessage()){
	  return userRequest.getUpdate().getMessage().getFrom().getFirstName();
	}
    return userRequest.getUpdate().getCallbackQuery().getFrom().getFirstName();
  }

  private void firstEnterHandle(UserRequest userRequest) {
	final Long chatId = userRequest.getChatId();
//	stickerService.
	//todo
	String token = "CAACAgIAAxkBAAEGRjFjYPXCrp0yRZdeOjiCZ1o5rvO9QAACGQAD6dgTKFdhEtpsYKrLKgQ";
	telegramService.sendMessage(chatId, Messages.HELLO);
	telegramService.sendSticker(chatId,token);

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
