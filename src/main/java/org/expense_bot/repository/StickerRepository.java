package org.expense_bot.repository;

import org.expense_bot.model.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StickerRepository extends JpaRepository<Sticker,Long> {

}
