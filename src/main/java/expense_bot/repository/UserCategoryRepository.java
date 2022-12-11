package expense_bot.repository;

import expense_bot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

  Optional<UserCategory> getByUserIdAndCategory(Long userId, String categoryName);

  List<UserCategory> getByUserId(Long userId);

}
