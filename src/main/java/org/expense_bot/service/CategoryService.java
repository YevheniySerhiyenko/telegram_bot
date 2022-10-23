package org.expense_bot.service;

import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;

import java.util.List;

public interface CategoryService {

  void addToUser(Long userId, String category);

  Category findByName(String name);

  Category create(Category category);

  List<UserCategory> getByUser(Long userId);

  List<Category> getDefault();

  void deleteFromUser(Long chatId, String category);

}
