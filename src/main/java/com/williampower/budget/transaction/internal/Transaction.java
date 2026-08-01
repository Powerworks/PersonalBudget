package com.williampower.budget.transaction.internal;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.williampower.budget.transaction.TransactionType;

@Table("transactions")
record Transaction(
		@Id Long id,
		TransactionType type,
		BigDecimal amount,
		String category,
		String description,
		LocalDate occurredOn,
		@CreatedDate Instant createdAt
) {

	static Transaction newTransaction(TransactionType type, BigDecimal amount, String category, String description, LocalDate occurredOn) {
		return new Transaction(null, type, amount, category, description, occurredOn, null);
	}

}
