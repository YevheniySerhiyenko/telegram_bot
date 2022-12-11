package expense_bot.service.impl;

import expense_bot.model.Income;
import expense_bot.repository.IncomeRepository;
import expense_bot.service.IncomeService;
import expense_bot.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

  private final IncomeRepository incomeRepository;

  @Override
  public Income save(Income income) {
    return incomeRepository.save(income);
  }

  @Override
  public List<Income> getAllCurrentMonth(Long userId) {
    return incomeRepository.getAllByUserIdAndIncomeDateIsAfter(userId, DateUtil.getStartOfMonth());
  }

  @Override
  public List<Income> getAll(Long userId, LocalDate date) {
    return incomeRepository.getAllBy(userId, date.getMonthValue(), date.getYear());
  }

}
