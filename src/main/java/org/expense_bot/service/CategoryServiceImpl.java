package org.expense_bot.service;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;
import org.expense_bot.repository.CategoryRepository;
import org.expense_bot.repository.UserCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final UserCategoryRepository userCategoryRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Optional<Category> findByName(String name) {
	return categoryRepository.getByNameLike(name);
  }

  @Override
  public Category create(Category category) {
	return categoryRepository.save(category);
  }

  @Override
  public List<UserCategory> getByUser(User userId) {
	return userCategoryRepository.getByChatId(userId);
  }

  @Override
  public List<Category> getDefault() {
	return categoryRepository.findAll();
  }

}
