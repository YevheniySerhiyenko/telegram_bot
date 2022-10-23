package org.expense_bot.service;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Category;
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
  public void addToUser(Long userId, String category) {
	final Category categoryByName = categoryRepository.getByNameLike(category);
	if(categoryByName != null){
	  final UserCategory userCategory = UserCategory.builder().userId(userId).category(categoryByName).build();
	  userCategoryRepository.save(userCategory);
	}
	else{
	  final Category newCategory = categoryRepository.save(Category.builder().name(category).build());
	  final UserCategory userCategory = UserCategory.builder().userId(userId).category(newCategory).build();
	  userCategoryRepository.save(userCategory);
	}
  }

  @Override
  public Category findByName(String name) {
	return categoryRepository.getByNameLike(name);
  }

  @Override
  public Category create(Category category) {
	return categoryRepository.save(category);
  }

  @Override
  public List<UserCategory> getByUser(Long userId) {
	return userCategoryRepository.getByUserId(userId);
  }

  @Override
  public void deleteFromUser(Long chatId, String category) {
	final Optional<UserCategory> userCategory = userCategoryRepository.getByUserIdAndCategoryLike(chatId, category);
	userCategory.ifPresent(value -> userCategoryRepository.deleteById(value.getId()));
  }

  @Override
  public List<Category> getDefault() {
	return categoryRepository.findAll();
  }

}
