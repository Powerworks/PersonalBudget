package com.williampower.budget.summary.internal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import com.williampower.budget.TestcontainersConfiguration;
import com.williampower.budget.transaction.TransactionRecorded;
import com.williampower.budget.transaction.TransactionType;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@Import(TestcontainersConfiguration.class)
class SummaryModuleTests {

	@Autowired
	AccountBalanceRepository balanceRepository;

	@Autowired
	MonthlySummaryRepository summaryRepository;

	@Test
	void incomeIncreasesBalanceAndMonthlyTotal(Scenario scenario) {
		var before = balanceRepository.findById(AccountBalance.SINGLETON_ID).orElseThrow().balance();
		var event = new TransactionRecorded(3L, TransactionType.INCOME, new BigDecimal("500.00"), "salary", LocalDate.now());
		var yearMonth = YearMonth.from(event.occurredOn()).toString();

		scenario.publish(event)
				.andWaitForStateChange(() -> summaryRepository.findByYearMonth(yearMonth).orElse(null))
				.andVerify(summary -> assertThat(summary.totalIncome()).isEqualByComparingTo("500.00"));

		assertThat(balanceRepository.findById(AccountBalance.SINGLETON_ID).orElseThrow().balance())
				.isEqualByComparingTo(before.add(new BigDecimal("500.00")));
	}

	@Test
	void expenseDecreasesBalance(Scenario scenario) {
		var before = balanceRepository.findById(AccountBalance.SINGLETON_ID).orElseThrow().balance();
		var event = new TransactionRecorded(4L, TransactionType.EXPENSE, new BigDecimal("75.00"), "groceries", LocalDate.now());
		var yearMonth = YearMonth.from(event.occurredOn()).toString();

		scenario.publish(event)
				.andWaitForStateChange(() -> summaryRepository.findByYearMonth(yearMonth).orElse(null))
				.andVerify(summary -> assertThat(summary.totalExpense()).isEqualByComparingTo("75.00"));

		assertThat(balanceRepository.findById(AccountBalance.SINGLETON_ID).orElseThrow().balance())
				.isEqualByComparingTo(before.subtract(new BigDecimal("75.00")));
	}

}
