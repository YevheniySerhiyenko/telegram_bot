package org.expense_bot.handler.stickers;

import lombok.RequiredArgsConstructor;
import org.expense_bot.enums.ConversationState;
import org.expense_bot.enums.StickerEditAction;
import org.expense_bot.handler.RequestHandler;
import org.expense_bot.model.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StickerFinalActionHandler extends RequestHandler {

  private final ApplicationContext context;

  @Override
  public boolean isApplicable(Request request) {
    return isStateEqual(request, ConversationState.Settings.WAITING_STICKERS_FINAL_ACTION);
  }

  @Override
  public void handle(Request request) {
    if(hasCallBack(request)) {
      final String updateData = getUpdateData(request).split(" ")[0];
      final StickerEditAction action = StickerEditAction.parseAction(updateData);
      final String stickerAction = request.getSession().getStickerAction().name();
      context.getBean(action.getHandler()).handle(request.getUserId(), stickerAction);
    }
  }

  @Override
  public boolean isGlobal() {
	return false;
  }

}
