package com.williampower.budget.summary.internal;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import com.williampower.budget.transaction.TransactionRecorded;
import com.williampower.budget.transaction.TransactionType;

@Service
class SummaryService {

	private final AccountBalanceRepository balanceRepository;
	private final MonthlySummaryRepository summaryRepository;

	SummaryService(AccountBalanceRepository balanceRepository, MonthlySummaryRepository summaryRepository) {
		this.balanceRepository = balanceRepository;
		this.summaryRepository = summaryRepository;
	}

	AccountBalance currentBalance() {
		return balanceRepository.findById(AccountBalance.SINGLETON_ID)
				.orElseThrow(() -> new IllegalStateException("Account balance row missing; schema.sql should have seeded it"));
	}

	Optional<MonthlySummary> findMonthlySummary(String yearMonth) {
		return summaryRepository.findByYearMonth(yearMonth);
	}

	@ApplicationModuleListener
	void on(TransactionRecorded event) {
		var yearMonth = YearMonth.from(event.occurredOn()).toString();

		var summary = summaryRepository.findByYearMonth(yearMonth).orElseGet(() -> MonthlySummary.empty(yearMonth));
		var balance = currentBalance();

		if (event.type() == TransactionType.INCOME) {
			summaryRepository.save(summary.plusIncome(event.amount()));
			balanceRepository.save(balance.plus(event.amount()));
		}
		else {
			summaryRepository.save(summary.plusExpense(event.amount()));
			balanceRepository.save(balance.minus(event.amount()));
		}
	}

}
