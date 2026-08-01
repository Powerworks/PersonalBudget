package com.williampower.budget.transaction.internal;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.williampower.budget.transaction.internal.dto.CreateTransactionRequest;
import com.williampower.budget.transaction.internal.dto.TransactionResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
class TransactionController {

	private final TransactionService service;

	TransactionController(TransactionService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	TransactionResponse create(@Valid @RequestBody CreateTransactionRequest request) {
		var saved = service.record(request.type(), request.amount(), request.category(), request.description(), request.occurredOn());
		return toResponse(saved);
	}

	@GetMapping
	List<TransactionResponse> findAll() {
		return service.findAll().stream().map(this::toResponse).toList();
	}

	private TransactionResponse toResponse(Transaction transaction) {
		return new TransactionResponse(
				transaction.id(),
				transaction.type(),
				transaction.amount(),
				transaction.category(),
				transaction.description(),
				transaction.occurredOn(),
				transaction.createdAt()
		);
	}

}
