package com.williampower.budget.summary.internal.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceResponse(
		BigDecimal balance,
		Instant updatedAt
) {
}
