package org.expense_bot.service;

import org.expense_bot.model.User;
import org.expense_bot.model.UserRequest;

import java.util.Optional;

public interface UserService {

  Optional<User> getByChatId(Long chatId);

  User checkUser(UserRequest userRequest);

}
