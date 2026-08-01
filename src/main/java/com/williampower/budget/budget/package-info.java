/**
 * The Budget module tracks per-category monthly spending limits.
 * It reacts to {@link com.williampower.budget.transaction.TransactionRecorded}
 * events published by the Transaction module and, in turn, publishes
 * {@link com.williampower.budget.budget.BudgetExceeded} events when a
 * category's spend for the month passes its configured limit.
 */
package com.williampower.budget.budget;
