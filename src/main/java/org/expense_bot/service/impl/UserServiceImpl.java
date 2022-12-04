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
	final Optional<User> user = getByUserId(request.getUserId());
	if(user.isEmpty()) {
	  firstEnteredHandler.handle(request);
	  userRepository.save(getUser(request));
	}
  }

  @Override
  public void updatePassword(Long userId, String password) {
	getByUserId(userId).ifPresent(user ->
	  userRepository.save(
		User.builder()
		  .userId(userId)
		  .name(user.getName())
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

  @Override
  public void disablePassword(Long userId) {
	getByUserId(userId).ifPresent(user ->
	  userRepository.save(
		User.builder()
		  .userId(userId)
		  .name(user.getName())
		  .enablePassword(false)
		  .password(null)
		  .build()));
  }

  @Override
  public void login(Long userId) {
	getByUserId(userId)
	  .ifPresent(user -> userRepository.save(
		User.builder()
		  .userId(userId)
		  .name(user.getName())
		  .password(user.getPassword())
		  .enablePassword(user.isEnablePassword())
		  .isLogined(true)
		  .build()));
  }

  @Override
  public Optional<User> getByUserId(Long userId) {
	return userRepository.findByUserId(userId);
  }

  private User getUser(Request request) {
	return User.builder()
	  .name(getFirstName(request))
	  .userId(request.getUserId())
	  .build();
  }

  private String getFirstName(Request request) {
	if(request.getUpdate().hasMessage()) {
	  return request.getUpdate().getMessage().getFrom().getFirstName();
	}
	return request.getUpdate().getCallbackQuery().getFrom().getFirstName();
  }

}
