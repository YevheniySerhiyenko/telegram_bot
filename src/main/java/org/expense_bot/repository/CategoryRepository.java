package org.expense_bot.repository;

import org.expense_bot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

  Optional<Category> getByNameLike(String name);

}
