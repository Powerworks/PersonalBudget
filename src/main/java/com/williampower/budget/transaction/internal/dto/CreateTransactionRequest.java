package com.williampower.budget.transaction.internal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.williampower.budget.transaction.TransactionType;

public record CreateTransactionRequest(
		@NotNull TransactionType type,
		@NotNull @Positive BigDecimal amount,
		@NotBlank String category,
		String description,
		@NotNull LocalDate occurredOn
) {
}
