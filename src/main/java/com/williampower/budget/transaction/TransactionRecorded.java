package com.williampower.budget.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Published whenever a new income or expense entry has been recorded.
 * This is the sole way other modules learn about transaction activity.
 */
public record TransactionRecorded(
		Long transactionId,
		TransactionType type,
		BigDecimal amount,
		String category,
		LocalDate occurredOn
) {
}
