package com.williampower.budget.budget.internal;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.williampower.budget.budget.internal.dto.BudgetResponse;
import com.williampower.budget.budget.internal.dto.BudgetStatusResponse;
import com.williampower.budget.budget.internal.dto.CreateBudgetRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/budgets")
class BudgetController {

	private final BudgetService service;

	BudgetController(BudgetService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	BudgetResponse createOrUpdate(@Valid @RequestBody CreateBudgetRequest request) {
		var saved = service.createOrUpdate(request.category(), request.monthlyLimit());
		return new BudgetResponse(saved.id(), saved.category(), saved.monthlyLimit());
	}

	@GetMapping
	List<BudgetResponse> findAll() {
		return service.findAll().stream()
				.map(b -> new BudgetResponse(b.id(), b.category(), b.monthlyLimit()))
				.toList();
	}

	@GetMapping("/{category}/status")
	BudgetStatusResponse status(@PathVariable String category, @RequestParam(required = false) String yearMonth) {
		var resolvedYearMonth = yearMonth != null ? yearMonth : YearMonth.now().toString();

		var spentAmount = service.findSpend(category, resolvedYearMonth)
				.map(CategorySpend::spentAmount)
				.orElse(BigDecimal.ZERO);

		var monthlyLimit = service.findAll().stream()
				.filter(b -> b.category().equals(category))
				.map(CategoryBudget::monthlyLimit)
				.findFirst()
				.orElse(BigDecimal.ZERO);

		return new BudgetStatusResponse(category, resolvedYearMonth, spentAmount, monthlyLimit);
	}

}
