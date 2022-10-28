package org.expense_bot.repository;

import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

  Optional<UserCategory> getByChatIdAndCategoryLike(Long chatId, String categoryName);

  List<UserCategory> getByChatId(Long userId);

}
