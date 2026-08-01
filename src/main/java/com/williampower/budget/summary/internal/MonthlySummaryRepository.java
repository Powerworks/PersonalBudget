package com.williampower.budget.summary.internal;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

interface MonthlySummaryRepository extends ListCrudRepository<MonthlySummary, Long> {

	Optional<MonthlySummary> findByYearMonth(String yearMonth);

}
