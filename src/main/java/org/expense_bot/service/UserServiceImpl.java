package org.expense_bot.service;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.User;
import org.expense_bot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User create(User user) {
	return userRepository.save(user);
  }

  @Override
  public Optional<User> getByChatId(Long chatId) {
	return userRepository.findByChatId(chatId);
  }

}
