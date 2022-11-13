package org.expense_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseGroup {

  private String category;

  private BigDecimal sum;

}
