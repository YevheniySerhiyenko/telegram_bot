package org.expense_bot.service;

import org.expense_bot.model.UserCategory;

import java.util.List;

public interface UserCategoryService {

  UserCategory add(Long userId, String categoryName);

  void delete(Long userId, String categoryName);

  List<UserCategory> getByUserId(Long userId);

}
