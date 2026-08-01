package com.williampower.budget.budget.internal;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("category_spend")
record CategorySpend(
		@Id Long id,
		String category,
		String yearMonth,
		BigDecimal spentAmount
) {

	static CategorySpend newSpend(String category, String yearMonth, BigDecimal amount) {
		return new CategorySpend(null, category, yearMonth, amount);
	}

	CategorySpend plus(BigDecimal amount) {
		return new CategorySpend(id, category, yearMonth, spentAmount.add(amount));
	}

}
