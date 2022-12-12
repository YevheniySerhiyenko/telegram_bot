package expense_bot.handler.categories.action_state;

import expense_bot.constant.Messages;
import expense_bot.enums.ConversationState;
import expense_bot.enums.StickerAction;
import expense_bot.exception.DuplicateException;
import expense_bot.keyboard.KeyboardBuilder;
import expense_bot.sender.StickerSender;
import expense_bot.service.UserCategoryService;
import expense_bot.service.impl.SessionService;
import expense_bot.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class CategoryAddNewHandler implements CategoryActionState {

  private final TelegramService telegramService;
  private final SessionService sessionService;
  private final StickerSender stickerSender;
  private final UserCategoryService userCategoryService;


  @Override
  public void handle(Long userId) {
    telegramService.sendMessage(userId, Messages.ENTER_CATEGORY_NAME, KeyboardBuilder.buildBackButton());
  }

  @Override
  public void handleFinal(Long userId, String categoryParam) {
    final ReplyKeyboard backButton = KeyboardBuilder.buildBackButton();
    try {
      userCategoryService.add(userId, categoryParam);
    } catch (DuplicateException exception) {
      telegramService.sendMessage(userId, Messages.ALREADY_HAD_SUCH_CATEGORY, backButton);
      stickerSender.sendSticker(userId, StickerAction.ALREADY_EXISTS_CATEGORY.name());
      sessionService.updateState(userId, ConversationState.Categories.WAITING_FINAL_ACTION);
      return;
    }
    telegramService.sendMessage(userId, Messages.CATEGORY_ADDED_TO_YOUR_LIST, backButton);
    sessionService.updateState(userId, ConversationState.Categories.WAITING_CATEGORY_ACTION);
  }

}
