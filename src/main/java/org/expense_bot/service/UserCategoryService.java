package org.expense_bot.service;

import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;

public interface UserCategoryService {

  UserCategory add(Long userId, Category category);

  void delete(Long userId, Category category);

}
