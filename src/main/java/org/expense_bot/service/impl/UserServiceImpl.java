package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.expense_bot.handler.NewUserHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.User;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final NewUserHandler firstEnteredHandler;
  @Value("${session_timeout}")
  private Integer sessionTimeout;

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
	{
	  user.setPassword(password);
	  user.setEnablePassword(true);
	  userRepository.save(user);
	});
  }

  @Override
  public boolean checkPassword(Long userId, String password) {
	return getByUserId(userId)
	  .map(value -> value.getPassword().equals(password))
	  .orElse(false);
  }

  @Override
  public void disablePassword(Long userId) {
	getByUserId(userId).ifPresent(user -> {
	  user.setEnablePassword(false);
	  userRepository.save(user);
	});
  }

  @Override
  public void login(Long userId) {
	getByUserId(userId).ifPresent(user -> {
	  user.setLogined(true);
	  userRepository.save(user);
	});
  }

  @Override
  public void closeSession(Long userId) {
	getByUserId(userId).ifPresent(user ->
	  {
		final LocalDateTime lastActionTime = user.getLastActionTime();
		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime dateTime = lastActionTime.plusMinutes(sessionTimeout);
		if(dateTime.isEqual(now) || dateTime.isBefore(now)) {
		  unLogin(userId);
		}
	  }
	);
  }

  @Override
  public void updateActionTime(Long userId) {
	getByUserId(userId).ifPresent(user -> {
	  user.setLastActionTime(LocalDateTime.now());
	  userRepository.save(user);
	});
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

  private void unLogin(Long userId) {
	getByUserId(userId).ifPresent(user -> {
	  if(user.isLogined()){
		user.setLogined(false);
		userRepository.save(user);
		log.info("User session by user {} closed", userId);
	  }
	});
  }

}
