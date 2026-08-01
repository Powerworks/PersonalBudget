package com.williampower.budget.transaction.internal.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import com.williampower.budget.transaction.TransactionType;

public record TransactionResponse(
		Long id,
		TransactionType type,
		BigDecimal amount,
		String category,
		String description,
		LocalDate occurredOn,
		Instant createdAt
) {
}
