package com.williampower.budget.summary.internal;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williampower.budget.summary.internal.dto.BalanceResponse;
import com.williampower.budget.summary.internal.dto.MonthlySummaryResponse;

@RestController
@RequestMapping("/api/summary")
class SummaryController {

	private final SummaryService service;

	SummaryController(SummaryService service) {
		this.service = service;
	}

	@GetMapping("/balance")
	BalanceResponse balance() {
		var balance = service.currentBalance();
		return new BalanceResponse(balance.balance(), balance.updatedAt());
	}

	@GetMapping("/monthly/{yearMonth}")
	MonthlySummaryResponse monthly(@PathVariable String yearMonth) {
		return service.findMonthlySummary(yearMonth)
				.map(s -> new MonthlySummaryResponse(s.yearMonth(), s.totalIncome(), s.totalExpense(), s.totalIncome().subtract(s.totalExpense())))
				.orElseGet(() -> new MonthlySummaryResponse(yearMonth, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
	}

}
