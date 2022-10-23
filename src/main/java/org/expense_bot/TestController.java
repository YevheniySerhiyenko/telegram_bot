package org.expense_bot;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Category;
import org.expense_bot.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final CategoryService categoryService;

  @GetMapping("/all")
  public List<Category> getAll(){
    return categoryService.getDefault();
  }

}
