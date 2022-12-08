package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class User {

  @Id
  private Long userId;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(unique = true)
  private String password;

  @Column(nullable = false)
  private boolean enablePassword;

  @Column(nullable = false)
  private boolean isLogined;

  @Column(nullable = false)
  private LocalDateTime lastActionTime;
}
