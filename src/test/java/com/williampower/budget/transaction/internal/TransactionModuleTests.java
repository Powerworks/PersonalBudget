package com.williampower.budget.transaction.internal;

import java.math.BigDecimal;
import java.time.LocalDate;

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
class TransactionModuleTests {

	@Autowired
	TransactionService service;

	@Test
	void recordingExpensePublishesTransactionRecorded(Scenario scenario) {
		scenario.stimulate(() -> service.record(TransactionType.EXPENSE, new BigDecimal("42.50"), "groceries", "weekly shop", LocalDate.now()))
				.andWaitForEventOfType(TransactionRecorded.class)
				.toArriveAndVerify(event -> {
					assertThat(event.type()).isEqualTo(TransactionType.EXPENSE);
					assertThat(event.amount()).isEqualByComparingTo("42.50");
					assertThat(event.category()).isEqualTo("groceries");
				});
	}

	@Test
	void recordedTransactionsCanBeListed() {
		service.record(TransactionType.INCOME, new BigDecimal("1000.00"), "salary", "monthly pay", LocalDate.now());

		assertThat(service.findAll())
				.extracting(Transaction::category)
				.contains("salary");
	}

}
