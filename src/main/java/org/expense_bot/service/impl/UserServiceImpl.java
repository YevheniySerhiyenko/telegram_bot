package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.handler.NewUserHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.User;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final NewUserHandler firstEnteredHandler;

  @Override
  public List<User> getAll() {
	return userRepository.findAll();
  }

  @Override
  public void checkUser(Request request) {
	final String firstName = getFirstName(request);
	final Optional<User> user = getByUserId(request.getUserId());
	if(user.isEmpty()) {
	  firstEnteredHandler.handle(request);
	}
	userRepository.save(getUser(request, firstName));
  }

  @Override
  public void updatePassword(Long userId, String password) {
    getByUserId(userId).ifPresent(user ->
	  userRepository.save(User.builder()
		.userId(userId).name(user.getName())
		.enablePassword(true)
		.password(password)
		.build()));
  }

  @Override
  public boolean checkPassword(Long userId, String password) {
	return getByUserId(userId)
	  .map(value -> value.getPassword().equals(password))
	  .orElse(false);
  }

  private String getFirstName(Request request) {
	if(request.getUpdate().hasMessage()) {
	  return request.getUpdate().getMessage().getFrom().getFirstName();
	}
	return request.getUpdate().getCallbackQuery().getFrom().getFirstName();
  }

  @Override
  public Optional<User> getByUserId(Long userId) {
	return userRepository.findByUserId(userId);
  }

  private User getUser(Request request, String firstName) {
	return User.builder()
	  .name(firstName)
	  .userId(request.getUserId())
	  .build();
  }

}
