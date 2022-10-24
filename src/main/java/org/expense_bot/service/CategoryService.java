package org.expense_bot.service;

import org.expense_bot.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

  Optional<Category> findByName(String name);

  Category create(Category category);

  List<Category> getDefault();

}
