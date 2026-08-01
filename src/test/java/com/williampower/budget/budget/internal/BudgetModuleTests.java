package com.williampower.budget.budget.internal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import com.williampower.budget.TestcontainersConfiguration;
import com.williampower.budget.budget.BudgetExceeded;
import com.williampower.budget.transaction.TransactionRecorded;
import com.williampower.budget.transaction.TransactionType;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@Import(TestcontainersConfiguration.class)
class BudgetModuleTests {

	@Autowired
	CategoryBudgetRepository budgetRepository;

	@Autowired
	CategorySpendRepository spendRepository;

	@Test
	void expenseExceedingLimitPublishesBudgetExceeded(Scenario scenario) {
		budgetRepository.save(CategoryBudget.newBudget("groceries", new BigDecimal("100.00")));

		var event = new TransactionRecorded(1L, TransactionType.EXPENSE, new BigDecimal("150.00"), "groceries", LocalDate.now());

		scenario.publish(event)
				.andWaitForEventOfType(BudgetExceeded.class)
				.toArriveAndVerify(exceeded -> {
					assertThat(exceeded.category()).isEqualTo("groceries");
					assertThat(exceeded.spentAmount()).isEqualByComparingTo("150.00");
					assertThat(exceeded.monthlyLimit()).isEqualByComparingTo("100.00");
				});
	}

	@Test
	void expenseWithinLimitRecordsSpendWithoutExceedingBudget(Scenario scenario) {
		budgetRepository.save(CategoryBudget.newBudget("entertainment", new BigDecimal("100.00")));

		var event = new TransactionRecorded(2L, TransactionType.EXPENSE, new BigDecimal("30.00"), "entertainment", LocalDate.now());
		var yearMonth = YearMonth.from(event.occurredOn()).toString();

		scenario.publish(event)
				.andWaitForStateChange(() -> spendRepository.findByCategoryAndYearMonth("entertainment", yearMonth).orElse(null))
				.andVerify(spend -> assertThat(spend.spentAmount()).isEqualByComparingTo("30.00"));
	}

}
