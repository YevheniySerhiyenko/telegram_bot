package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.constant.Constants;
import org.expense_bot.constant.Messages;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.model.UserCategory;
import org.expense_bot.model.UserSession;
import org.expense_bot.repository.ExpenseRepository;
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
  private final ExpenseRepository expenseRepository;
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
    final Optional<UserCategory> category = repository.getByUserIdAndCategory(chatId, categoryName);
    category.ifPresent(userCategory -> checkExpenses(chatId, userCategory));
    final String message = String.format(Messages.CATEGORY_DELETED, categoryName);
    telegramService.sendMessage(chatId, message + Constants.BUTTON_TRASH);
  }

  private void checkExpenses(Long chatId, UserCategory userCategory) {
    //todo check if expenses exist
    repository.delete(userCategory);
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
      final UserSession session = sessionService.getSession(chatId);
      session.setState(ConversationState.Categories.WAITING_FINAL_ACTION);
      sessionService.saveSession(session);
      throw new RuntimeException(Messages.ALREADY_HAD_SUCH_CATEGORY);
    }
  }

  public String getSticker(Long chatId) {
    return stickerSender.getSticker(chatId, Messages.ERROR_STICKER_MESSAGE);
  }

}
