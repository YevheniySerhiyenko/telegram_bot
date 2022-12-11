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

  @Column(nullable = false, unique = true)
  private String category;

  @Column(nullable = false)
  private Long userId;

}
