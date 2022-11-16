package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.Sticker;
import org.expense_bot.repository.StickerRepository;
import org.expense_bot.service.StickerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StickerServiceImpl implements StickerService {

  private final StickerRepository stickerRepository;

  @Override
  public Optional<Sticker> getOne(String action) {
	return stickerRepository.findByEnabledIsTrueAndActionLike(action);
  }

  @Override
  public void setEnable(Boolean enable, String action, Long userId) {
    stickerRepository.setEnabled(enable, action, userId);
  }

  @Override
  public List<Sticker> getAll() {
	return stickerRepository.findAll();
  }

}
