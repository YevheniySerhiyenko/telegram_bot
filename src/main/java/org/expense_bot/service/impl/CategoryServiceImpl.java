package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Category;
import org.expense_bot.repository.CategoryRepository;
import org.expense_bot.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Category findByName(String name) {
	return categoryRepository.getByNameLike(name);
  }

  @Override
  public Category create(Category category) {
	return categoryRepository.save(category);
  }

  @Override
  public List<Category> getDefault() {
	return categoryRepository.findAll();
  }

}
