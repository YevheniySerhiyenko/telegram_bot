package org.expense_bot.service;

import org.expense_bot.model.User;

import java.util.Optional;

public interface UserService {

  Optional<User> getByChatId(Long chatId);

  User create(User user);

}
