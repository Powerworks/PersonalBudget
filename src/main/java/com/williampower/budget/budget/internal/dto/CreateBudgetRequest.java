package com.williampower.budget.budget.internal.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBudgetRequest(
		@NotBlank String category,
		@NotNull @Positive BigDecimal monthlyLimit
) {
}
