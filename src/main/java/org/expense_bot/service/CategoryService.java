package org.expense_bot.service;

import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

  Optional<Category> findByName(String name);

  Category create(Category category);

  List<Category> getDefault();

}
