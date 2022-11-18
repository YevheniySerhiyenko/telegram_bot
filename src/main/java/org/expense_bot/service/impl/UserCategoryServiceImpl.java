package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Buttons;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerAction;
import org.expense_bot.model.UserCategory;
import org.expense_bot.repository.UserCategoryRepository;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.UserCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {

  private final UserCategoryRepository repository;
  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final StickerSender stickerSender;

  @Override
  public UserCategory add(Long userId, String categoryName) {
    existByUser(userId, categoryName);
    telegramService.sendMessage(userId, Messages.SUCCESS);
    return repository.save(getCategory(userId, categoryName));
  }

  @Override
  public void delete(Long userId, String categoryName) {
    repository.getByUserIdAndCategory(userId, categoryName)
      .ifPresent(repository::delete);
    telegramService.sendMessage(userId, Buttons.BUTTON_TRASH.getValue());
  }

  @Override
  public List<UserCategory> getByUserId(Long userId) {
    return repository.getByUserId(userId);
  }

  private void existByUser(Long userId, String category) {
    repository.getByUserIdAndCategory(userId, category).ifPresent(value -> {
      stickerSender.sendSticker(userId, StickerAction.ALREADY_EXISTS_CATEGORY.name());
      telegramService.sendMessage(userId, Messages.ALREADY_HAD_SUCH_CATEGORY);
      sessionService.updateState(userId, ConversationState.Categories.WAITING_FINAL_ACTION);
      throw new RuntimeException(Messages.ALREADY_HAD_SUCH_CATEGORY);
    });
  }

  private UserCategory getCategory(Long userId, String categoryName) {
    return UserCategory.builder()
      .userId(userId)
      .category(categoryName)
      .build();
  }

}
