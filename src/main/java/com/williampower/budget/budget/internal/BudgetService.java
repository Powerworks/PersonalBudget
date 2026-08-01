package com.williampower.budget.budget.internal;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williampower.budget.budget.BudgetExceeded;
import com.williampower.budget.transaction.TransactionRecorded;
import com.williampower.budget.transaction.TransactionType;

@Service
class BudgetService {

	private final CategoryBudgetRepository budgetRepository;
	private final CategorySpendRepository spendRepository;
	private final ApplicationEventPublisher events;

	BudgetService(CategoryBudgetRepository budgetRepository, CategorySpendRepository spendRepository, ApplicationEventPublisher events) {
		this.budgetRepository = budgetRepository;
		this.spendRepository = spendRepository;
		this.events = events;
	}

	@Transactional
	CategoryBudget createOrUpdate(String category, BigDecimal monthlyLimit) {
		var toSave = budgetRepository.findByCategory(category)
				.map(existing -> new CategoryBudget(existing.id(), category, monthlyLimit))
				.orElseGet(() -> CategoryBudget.newBudget(category, monthlyLimit));

		return budgetRepository.save(toSave);
	}

	List<CategoryBudget> findAll() {
		return budgetRepository.findAll();
	}

	Optional<CategorySpend> findSpend(String category, String yearMonth) {
		return spendRepository.findByCategoryAndYearMonth(category, yearMonth);
	}

	@ApplicationModuleListener
	void on(TransactionRecorded event) {
		if (event.type() != TransactionType.EXPENSE) {
			return;
		}

		var yearMonth = YearMonth.from(event.occurredOn()).toString();

		var updatedSpend = spendRepository.findByCategoryAndYearMonth(event.category(), yearMonth)
				.map(existing -> existing.plus(event.amount()))
				.orElseGet(() -> CategorySpend.newSpend(event.category(), yearMonth, event.amount()));

		var savedSpend = spendRepository.save(updatedSpend);

		budgetRepository.findByCategory(event.category()).ifPresent(budget -> {
			if (savedSpend.spentAmount().compareTo(budget.monthlyLimit()) > 0) {
				events.publishEvent(new BudgetExceeded(event.category(), yearMonth, savedSpend.spentAmount(), budget.monthlyLimit()));
			}
		});
	}

}
