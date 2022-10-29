package org.expense_bot.repository;

import org.expense_bot.model.UserSticker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStickerRepository extends JpaRepository<UserSticker,Long> {

  Optional<UserSticker> findByEnabledIsTrueAndActionLikeAndUserId(String action,Long userId);

  UserSticker findByActionLikeAndUserId(String action,Long userId);

  List<UserSticker> findAllByUserId(Long userId);

  void deleteAllByUserId(Long userId);

  void deleteByActionLikeAndUserId(String action, Long userId);

}
