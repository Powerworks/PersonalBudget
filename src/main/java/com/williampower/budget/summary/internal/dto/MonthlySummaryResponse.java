package com.williampower.budget.summary.internal.dto;

import java.math.BigDecimal;

public record MonthlySummaryResponse(
		String yearMonth,
		BigDecimal totalIncome,
		BigDecimal totalExpense,
		BigDecimal net
) {
}
