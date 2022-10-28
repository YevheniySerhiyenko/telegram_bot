package org.expense_bot.service;

import org.expense_bot.model.Category;

import java.util.List;

public interface CategoryService {

  Category findByName(String name);

  Category create(Category category);

  List<Category> getDefault();

}
