package org.expense_bot.repository;

import org.expense_bot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

  Optional<UserCategory> getByUserIdAndCategoryLike(Long userId, String category);

  List<UserCategory> getByUserId(Long userId);

}
