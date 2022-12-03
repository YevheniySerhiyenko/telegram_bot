package org.expense_bot.exception;

import lombok.RequiredArgsConstructor;
import org.expense_bot.sender.StickerSender;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;


@Component
@RequiredArgsConstructor
class ExceptionHandler extends DefaultHandlerExceptionResolver {

  private final TelegramService telegramService;
  private final StickerSender stickerSender;

}