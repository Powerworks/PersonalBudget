package com.williampower.budget.summary.internal;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("monthly_summary")
record MonthlySummary(
		@Id Long id,
		String yearMonth,
		BigDecimal totalIncome,
		BigDecimal totalExpense
) {

	static MonthlySummary empty(String yearMonth) {
		return new MonthlySummary(null, yearMonth, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	MonthlySummary plusIncome(BigDecimal amount) {
		return new MonthlySummary(id, yearMonth, totalIncome.add(amount), totalExpense);
	}

	MonthlySummary plusExpense(BigDecimal amount) {
		return new MonthlySummary(id, yearMonth, totalIncome, totalExpense.add(amount));
	}

}
