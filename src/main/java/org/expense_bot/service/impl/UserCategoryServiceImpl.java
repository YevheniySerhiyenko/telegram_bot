package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Messages;
import org.expense_bot.model.Category;
import org.expense_bot.model.User;
import org.expense_bot.model.UserCategory;
import org.expense_bot.repository.CategoryRepository;
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
  private final CategoryRepository categoryRepository;
  private final TelegramService telegramService;

  @Override
  public UserCategory add(Long chatId, String categoryName) {
    final User user = getUser(chatId);
    final Category category = getCategory(chatId,categoryName);
    checkCategory(chatId, category);
    final UserCategory userCategory = UserCategory.builder()
      .chatId(user)
      .category(category)
      .build();
    return repository.save(userCategory);
  }

  private User getUser(Long chatId) {
    return userRepository.findByChatId(chatId)
      .orElseThrow(() -> {
        final String message = Messages.USER_NOT_FOUND + chatId;
        telegramService.sendMessage(chatId, Messages.ERROR);
        telegramService.sendMessage(chatId, message);
        return new RuntimeException(message);
      });
  }

  @Override
  public void delete(Long chatId, String categoryName) {
    final User user = getUser(chatId);
    final Category category = getCategory(chatId, categoryName);
    repository.getByChatIdAndCategoryLike(user, category)
      .ifPresent(repository::delete);
    telegramService.sendMessage(chatId, Messages.SUCCESS);
    telegramService.sendMessage(chatId, Messages.CATEGORY_DELETED);
  }

  @Override
  public List<UserCategory> getByUser(User userId) {
    return repository.getByChatId(userId);
  }

  @Override
  public boolean existByUser(Long chatId, Category category) {
    final User user = getUser(chatId);
    final Optional<UserCategory> userCategory = repository.getByChatIdAndCategoryLike(user, category);

    if(userCategory.isPresent()) {
      telegramService.sendMessage(chatId, Messages.ERROR);
      telegramService.sendMessage(chatId, Messages.ALREADY_HAD_SUCH_CATEGORY);
      throw new RuntimeException(Messages.ALREADY_HAD_SUCH_CATEGORY);
    }

    return true;
  }

  private Category getCategory(Long chatId, String categoryParam) {
    return categoryRepository.getByNameLike(categoryParam)
      .orElseThrow(() -> {
        telegramService.sendMessage(chatId,Messages.CATEGORY_NOT_FOUND);
        throw new RuntimeException(Messages.CATEGORY_NOT_FOUND);
      });
  }

  private void checkCategory(Long chatId, Category category) {
      existByUser(chatId, category);
      telegramService.sendMessage(chatId, Messages.SUCCESS);
      telegramService.sendMessage(chatId, Messages.CATEGORY_ADDED_FROM_DEFAULT);
  }

  private Category buildCategory(String categoryParam) {
    return Category.builder()
      .name(categoryParam)
      .build();
  }

}
