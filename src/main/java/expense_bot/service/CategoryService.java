package expense_bot.service;

import expense_bot.model.Category;

import java.util.List;

public interface CategoryService {

  Category findByName(String name);

  Category create(Category category);

  List<Category> getDefault();

}
