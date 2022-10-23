package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_categories")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserCategory {
  @Id
  @GeneratedValue
  private Long id;
  @JoinColumn(nullable = false,name = "category")
  @OneToOne(fetch = FetchType.EAGER,targetEntity = Category.class)
  private Category category;

  @Column(nullable = false)
  private Long userId;
}
