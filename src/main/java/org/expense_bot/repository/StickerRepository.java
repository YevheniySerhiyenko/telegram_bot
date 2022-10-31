package org.expense_bot.repository;

import org.expense_bot.model.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StickerRepository extends JpaRepository<Sticker,Long> {

  Optional<Sticker> findByEnabledIsTrueAndActionLike(String action);

  @Query("update Sticker set enabled = ?1 where action = ?2 and userId = ?3")
  void setEnabled(Boolean isEnabled, String action, Long userId);


  @Query("select s from Sticker as s left join UserSticker as us" +
    " on s.userId = us.userId  where s.userId = ?1 and  us.userId = ?1")
  List<Sticker> getAllByUserId(Long userId);

}
