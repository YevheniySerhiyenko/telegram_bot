package expense_bot.model;

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

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "incomes")

public class Income {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private BigDecimal sum;

  @Column(nullable = false)
  private LocalDateTime incomeDate;

  @Column(nullable = false)
  private Long userId;

}
