package com.williampower.budget.summary.internal;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("account_balance")
record AccountBalance(
		@Id Long id,
		BigDecimal balance,
		Instant updatedAt
) {

	static final Long SINGLETON_ID = 1L;

	AccountBalance plus(BigDecimal amount) {
		return new AccountBalance(id, balance.add(amount), Instant.now());
	}

	AccountBalance minus(BigDecimal amount) {
		return new AccountBalance(id, balance.subtract(amount), Instant.now());
	}

}
