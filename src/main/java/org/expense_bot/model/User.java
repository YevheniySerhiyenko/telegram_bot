package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  private Long userId;

  @Column(nullable = false, unique = true)
  private String name;

}
