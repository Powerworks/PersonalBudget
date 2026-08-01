package com.williampower.budget.budget.internal;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("category_budgets")
record CategoryBudget(
		@Id Long id,
		String category,
		BigDecimal monthlyLimit
) {

	static CategoryBudget newBudget(String category, BigDecimal monthlyLimit) {
		return new CategoryBudget(null, category, monthlyLimit);
	}

}
