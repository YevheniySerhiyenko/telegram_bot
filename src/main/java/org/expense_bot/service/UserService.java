package org.expense_bot.service;

import org.expense_bot.model.Request;
import org.expense_bot.model.User;

import java.util.List;

public interface UserService {

  List<User> getAll();

  void checkUser(Request request);

}
