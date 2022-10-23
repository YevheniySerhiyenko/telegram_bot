package org.expense_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
  @Column(name = "category")
  private String category;
  @Column(name = "sum")
  private Double sum;
  @Column(name = "date_time")
  private LocalDateTime dateTime;

}
