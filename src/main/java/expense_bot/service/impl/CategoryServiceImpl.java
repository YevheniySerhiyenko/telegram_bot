package expense_bot.service.impl;

import expense_bot.model.Category;
import expense_bot.repository.CategoryRepository;
import expense_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
