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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Expense {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "category", nullable = false)
  private String category;

  @Column(name = "sum", nullable = false)
  private BigDecimal sum;

  @Column(name = "date_time", nullable = false)
  private LocalDateTime dateTime;

}
