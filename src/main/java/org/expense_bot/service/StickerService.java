package org.expense_bot.service;

import org.expense_bot.model.Sticker;

public interface StickerService {

  Sticker save(Sticker sticker);

  Sticker get(String action);

  Sticker setEnable(Boolean enable);

}
