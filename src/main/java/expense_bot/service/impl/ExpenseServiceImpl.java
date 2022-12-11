package expense_bot.service.impl;

import expense_bot.enums.Button;
import expense_bot.model.Expense;
import expense_bot.repository.ExpenseRepository;
import expense_bot.service.ExpenseService;
import expense_bot.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;

  @Override
  public Expense save(Expense expense) {
    return expenseRepository.save(expense);
  }

  @Override
  public List<Expense> getByPeriod(Long userId, LocalDateTime from, LocalDateTime to, String category) {
    final LocalDateTime tomorrowMidnight = DateUtil.getTomorrowMidnight(to.toLocalDate());
    return category.equals(Button.BY_ALL_CATEGORIES.getValue())
      ? expenseRepository.getAllByUserIdAndDateTimeBetween(userId, from, tomorrowMidnight)
      : expenseRepository.getAllByUserIdAndDateTimeBetweenAndCategory(userId, from, tomorrowMidnight, category);
  }

  @Override
  public Expense update(Long userId, Expense expense) {
    return expenseRepository.save(expense);
  }

}
