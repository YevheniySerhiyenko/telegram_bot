package expense_bot.repository;

import expense_bot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Category getByNameLike(String name);

}
