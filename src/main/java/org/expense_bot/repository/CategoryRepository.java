package org.expense_bot.repository;

import org.expense_bot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Category getByNameLike(String name);

}
