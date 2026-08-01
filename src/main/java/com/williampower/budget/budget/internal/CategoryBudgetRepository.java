package com.williampower.budget.budget.internal;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

interface CategoryBudgetRepository extends ListCrudRepository<CategoryBudget, Long> {

	Optional<CategoryBudget> findByCategory(String category);

}
