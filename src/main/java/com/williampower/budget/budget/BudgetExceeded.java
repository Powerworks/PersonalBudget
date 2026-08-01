package com.williampower.budget.budget;

import java.math.BigDecimal;

/**
 * Published when recorded spend for a category in a given month
 * (format {@code yyyy-MM}) exceeds the configured monthly limit.
 */
public record BudgetExceeded(
		String category,
		String yearMonth,
		BigDecimal spentAmount,
		BigDecimal monthlyLimit
) {
}
