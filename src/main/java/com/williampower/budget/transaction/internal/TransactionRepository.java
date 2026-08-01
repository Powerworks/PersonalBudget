package com.williampower.budget.transaction.internal;

import org.springframework.data.repository.ListCrudRepository;

interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
}
