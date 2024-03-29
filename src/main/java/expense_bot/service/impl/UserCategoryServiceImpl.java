package expense_bot.service.impl;

import expense_bot.constant.Messages;
import expense_bot.enums.Button;
import expense_bot.exception.DuplicateException;
import expense_bot.model.UserCategory;
import expense_bot.repository.UserCategoryRepository;
import expense_bot.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {

  private final UserCategoryRepository repository;
  private final TelegramService telegramService;

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
    telegramService.sendMessage(userId, Button.BUTTON_TRASH.getValue());
  }

  @Override
  public List<UserCategory> getByUserId(Long userId) {
    return repository.getByUserId(userId);
  }

  private void existByUser(Long userId, String category) {
    repository.getByUserIdAndCategory(userId, category)
      .ifPresent(value -> {
        throw new DuplicateException(Messages.ALREADY_HAD_SUCH_CATEGORY);
      });
  }

  private UserCategory getCategory(Long userId, String categoryName) {
    return UserCategory.builder()
      .userId(userId)
      .category(categoryName)
      .build();
  }

}
