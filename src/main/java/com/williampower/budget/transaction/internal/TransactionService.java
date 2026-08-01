package com.williampower.budget.transaction.internal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williampower.budget.transaction.TransactionRecorded;
import com.williampower.budget.transaction.TransactionType;

@Service
class TransactionService {

	private final TransactionRepository repository;
	private final ApplicationEventPublisher events;

	TransactionService(TransactionRepository repository, ApplicationEventPublisher events) {
		this.repository = repository;
		this.events = events;
	}

	@Transactional
	Transaction record(TransactionType type, BigDecimal amount, String category, String description, LocalDate occurredOn) {
		var saved = repository.save(Transaction.newTransaction(type, amount, category, description, occurredOn));

		events.publishEvent(new TransactionRecorded(saved.id(), saved.type(), saved.amount(), saved.category(), saved.occurredOn()));

		return saved;
	}

	List<Transaction> findAll() {
		return repository.findAll();
	}

}
