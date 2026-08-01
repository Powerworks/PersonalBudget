package com.williampower.budget.budget.internal.dto;

import java.math.BigDecimal;

public record BudgetStatusResponse(
		String category,
		String yearMonth,
		BigDecimal spentAmount,
		BigDecimal monthlyLimit
) {
}
