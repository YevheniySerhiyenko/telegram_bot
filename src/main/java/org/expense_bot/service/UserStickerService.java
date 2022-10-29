package org.expense_bot.service;

import org.expense_bot.model.UserSticker;

import java.util.List;
import java.util.Optional;

public interface UserStickerService {

  UserSticker save(Long userId, String action, String token);

  Optional<UserSticker> getOne(String action, Long userId);

  List<UserSticker> getAll(Long userId);

  void delete(Long userId, String action);

  void deleteAll(Long userId);

}
