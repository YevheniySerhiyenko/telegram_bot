package expense_bot.service.impl;

import expense_bot.constant.ErrorMessages;
import expense_bot.exception.UserNotFoundException;
import expense_bot.handler.NewUserHandler;
import expense_bot.model.Request;
import expense_bot.model.User;
import expense_bot.repository.UserRepository;
import expense_bot.service.UserService;
import expense_bot.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final SessionService sessionService;

  @Value("${session_timeout}")
  private Integer sessionTimeout;

  @Override
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @Override
  public User getUser(Long userId) {
    return getOptional(userId).orElseThrow(
      () -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND + userId));
  }

  @Override
  public void checkUser(Request request) {
    final Optional<User> user = getOptional(request.getUserId());
    if (user.isEmpty()) {
      firstEnteredHandler.handle(request);
      userRepository.save(getUser(request));
    }
  }

  @Override
  public void updatePassword(Long userId, String password, Boolean enablePassword) {
    final User user = getUser(userId);
    user.setPassword(password);
    user.setEnablePassword(enablePassword);
    userRepository.save(user);
  }

  @Override
  public void login(Long userId) {
    final User user = getUser(userId);
    user.setLogined(true);
    userRepository.save(user);
  }

  @Override
  public void closeSession(Long userId) {
    final LocalDateTime lastActionTime = getUser(userId).getLastActionTime();
    final LocalDateTime now = LocalDateTime.now();
    final LocalDateTime dateTime = lastActionTime.plusMinutes(sessionTimeout);
    if (dateTime.isEqual(now) || dateTime.isBefore(now)) {
      unLogin(userId);
      sessionService.delete(userId);
    }
  }

  @Override
  public void updateActionTime(Long userId) {
    final User user = getUser(userId);
    user.setLastActionTime(LocalDateTime.now());
    userRepository.save(user);
  }

  @Override
  public Optional<User> getOptional(Long userId) {
    return userRepository.findByUserId(userId);
  }

  private User getUser(Request request) {
    return User.builder()
      .name(Utils.getFirstName(request))
      .userId(request.getUserId())
      .build();
  }

  private void unLogin(Long userId) {
    final User user = getUser(userId);
    if (!user.isLogined()) {
      return;
    }
    user.setLogined(false);
    userRepository.save(user);
    log.info("User session by user {} closed", userId);
  }

}
