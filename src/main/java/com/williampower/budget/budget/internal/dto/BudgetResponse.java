package com.williampower.budget.budget.internal.dto;

import java.math.BigDecimal;

public record BudgetResponse(
		Long id,
		String category,
		BigDecimal monthlyLimit
) {
}
