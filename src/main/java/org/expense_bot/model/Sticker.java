package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "stickers")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Sticker {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private String action;

  @Column(nullable = false)
  private boolean enabled;

  @Column(nullable = false)
  private Long userId;

}
