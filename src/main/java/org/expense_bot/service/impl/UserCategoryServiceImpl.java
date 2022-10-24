package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;
import org.expense_bot.repository.UserCategoryRepository;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.service.UserCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {

  private final UserCategoryRepository repository;
  private final UserRepository userRepository;
  private final TelegramService telegramService;

  @Override
  public UserCategory add(Long chatId, Category category) {
    final User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new RuntimeException("User not found by chat id: " + chatId));
    final Optional<UserCategory> userCategory = repository.getByChatIdAndCategoryLike(user, category);
    if(userCategory.isPresent()){
      telegramService.sendMessage(chatId,"Ви вже маєте дану категорію");
      return null;
    }
    return repository.save(getCategory(category, user));
  }

  private UserCategory getCategory(Category category, User user) {
    return UserCategory.builder()
      .chatId(user)
      .category(category)
      .build();
  }

  @Override
  public void delete(Long chatId, Category category) {
    final User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new RuntimeException("User not found by chat id: " + chatId));

    repository.getByChatIdAndCategoryLike(user,category)
      .ifPresent(repository::delete);
  }

  @Override
  public List<UserCategory> getByUser(User userId) {
    return repository.getByChatId(userId);
  }

}
