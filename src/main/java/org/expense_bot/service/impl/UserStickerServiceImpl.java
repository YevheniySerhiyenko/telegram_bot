package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.model.UserSticker;
import org.expense_bot.repository.UserStickerRepository;
import org.expense_bot.service.UserStickerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserStickerServiceImpl implements UserStickerService {

  private final UserStickerRepository userStickerRepository;

  @Override
  public UserSticker save(Long userId, String action, String token) {
	return userStickerRepository.save(buildSticker(userId, action, token));
  }

  @Override
  public Optional<UserSticker> getOne(String action, Long userId) {
	return userStickerRepository.findByEnabledIsTrueAndActionLikeAndUserId(action, userId);
  }

  @Override
  public List<UserSticker> getAll(Long userId) {
    return userStickerRepository.findAllByUserId(userId);
  }

  @Override
  public void delete(Long userId, String action) {
	final UserSticker userSticker = userStickerRepository.findByActionLikeAndUserId(action, userId);
	userStickerRepository.delete(userSticker);
  }

  @Override
  public void deleteAll(Long userId) {
	userStickerRepository.deleteAllByUserId(userId);
  }

  private UserSticker buildSticker(Long userId, String action, String token) {
	return UserSticker.builder()
	  .userId(userId)
	  .action(action)
	  .enabled(true)
	  .token(token)
	  .build();
  }

}
