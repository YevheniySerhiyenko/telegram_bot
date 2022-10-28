package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Sticker;
import org.expense_bot.repository.StickerRepository;
import org.expense_bot.service.StickerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StickerServiceImpl implements StickerService {

  private final StickerRepository stickerRepository;

  @Override
  public Sticker save(Sticker sticker) {
	return null;
  }

  @Override
  public Sticker get(String action) {
	return null;
  }

  @Override
  public Sticker setEnable(Boolean enable) {
	return null;
  }

}
