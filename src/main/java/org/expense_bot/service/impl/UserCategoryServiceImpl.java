package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.UserCategory;
import org.expense_bot.repository.UserCategoryRepository;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.UserCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {

  private final UserCategoryRepository repository;
  private final TelegramService telegramService;
  private final UserSessionService sessionService;
  private final StickerSender stickerSender;

  @Override
  public UserCategory add(Long chatId, String categoryName) {
    existByUser(chatId, categoryName);
    telegramService.sendMessage(chatId, Messages.SUCCESS);
    final UserCategory userCategory = UserCategory.builder()
      .userId(chatId)
      .category(categoryName)
      .build();
    return repository.save(userCategory);
  }

  @Override
  public void delete(Long chatId, String categoryName) {
    repository.getByUserIdAndCategory(chatId, categoryName)
      .ifPresent(repository::delete);
    final String message = String.format(Messages.CATEGORY_DELETED, categoryName);
    telegramService.sendMessage(chatId, message + Constants.BUTTON_TRASH);
  }

  @Override
  public List<UserCategory> getByUserId(Long userId) {
    return repository.getByUserId(userId);
  }

  private void existByUser(Long chatId, String category) {
    final Optional<UserCategory> userCategory = repository.getByUserIdAndCategory(chatId, category);

    if(userCategory.isPresent()) {
      telegramService.sendSticker(chatId, getSticker(chatId));
      telegramService.sendMessage(chatId, Messages.ALREADY_HAD_SUCH_CATEGORY);
      sessionService.updateState(chatId, ConversationState.Categories.WAITING_FINAL_ACTION);
      throw new RuntimeException(Messages.ALREADY_HAD_SUCH_CATEGORY);
    }
  }

  public String getSticker(Long chatId) {
    return stickerSender.getSticker(chatId, Messages.ERROR_STICKER_MESSAGE);
  }

}
