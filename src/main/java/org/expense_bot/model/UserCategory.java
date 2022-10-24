package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @JoinColumn(name = "category",referencedColumnName = "name")
  @OneToOne(targetEntity = Category.class)
  private Category category;

  @JoinColumn(name = "user_id")
  @OneToOne(targetEntity = User.class)
  private User chatId;

}
