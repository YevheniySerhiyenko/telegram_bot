package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.Category;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserSession;
import org.expense_bot.repository.CategoryRepository;
import org.expense_bot.repository.UserCategoryRepository;
import org.expense_bot.service.UserCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {

  private final UserCategoryRepository repository;
  private final CategoryRepository categoryRepository;
  private final TelegramService telegramService;
  private final UserSessionService sessionService;

  @Override
  public UserCategory add(Long chatId, String categoryName) {
    final Category category = getCategory(chatId, categoryName);
    if(category != null) {
      checkExistByUser(chatId, category);
    }
    telegramService.sendMessage(chatId, Messages.SUCCESS);
    telegramService.sendMessage(chatId, Messages.CATEGORY_ADDED_TO_YOUR_LIST);
    final UserCategory userCategory = UserCategory.builder()
      .chatId(chatId)
      .category(category == null ? categoryName : category.getName())
      .build();
    return repository.save(userCategory);
  }

  @Override
  public void delete(Long chatId, String categoryName) {
    repository.getByChatIdAndCategoryLike(chatId, categoryName)
      .ifPresent(repository::delete);
    final String message = String.format(Messages.CATEGORY_DELETED, categoryName);
    telegramService.sendMessage(chatId, message + Constants.BUTTON_DELETE);
  }

  @Override
  public List<UserCategory> getByUserId(Long userId) {
    return repository.getByChatId(userId);
  }

  private void checkExistByUser(Long chatId, Category category) {
    final Optional<UserCategory> userCategory = repository.getByChatIdAndCategoryLike(chatId, category.getName());

    if(userCategory.isPresent()) {
      telegramService.sendMessage(chatId, Messages.ERROR);
      telegramService.sendMessage(chatId, Messages.ALREADY_HAD_SUCH_CATEGORY);
      this.telegramService.sendMessage(chatId, Messages.CHOOSE_ACTION);
      final UserSession session = sessionService.getSession(chatId);
      session.setState(ConversationState.WAITING_CATEGORY_ACTION);
      sessionService.saveSession(chatId, session);
      throw new RuntimeException(Messages.ALREADY_HAD_SUCH_CATEGORY);
    }
  }

  private Category getCategory(Long chatId, String categoryParam) {
    final Category defaultCategory = categoryRepository.getByNameLike(categoryParam);
    if(defaultCategory != null) {
      telegramService.sendMessage(chatId, Messages.CATEGORY_FOUND_IN_DEFAULT);
      return defaultCategory;
    }
    return null;
  }
}
