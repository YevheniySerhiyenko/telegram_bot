package expense_bot.service;

import expense_bot.model.Request;
import expense_bot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

  List<User> getAll();

  User getUser(Long userId);

  Optional<User> getOptional(Long userId);

  void checkUser(Request request);

  void updatePassword(Long userId, String password, Boolean enablePassword);

  void login(Long userId);

  void closeSession(Long userId);

  void updateActionTime(Long userId);

}
