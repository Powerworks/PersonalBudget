package com.williampower.budget.budget.internal;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

interface CategorySpendRepository extends ListCrudRepository<CategorySpend, Long> {

	Optional<CategorySpend> findByCategoryAndYearMonth(String category, String yearMonth);

}
