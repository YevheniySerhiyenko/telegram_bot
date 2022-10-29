package org.expense_bot.service;

import org.expense_bot.model.Sticker;

import java.util.Optional;

public interface StickerService {

  Optional<Sticker> getOne(String action);

  void setEnable(Boolean enable, String action, Long userId);

}
