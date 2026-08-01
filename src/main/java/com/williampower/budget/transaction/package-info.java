/**
 * The Transaction module owns recording of income and expense entries.
 * It is the source of truth for money movements and publishes
 * {@link com.williampower.budget.transaction.TransactionRecorded} events
 * for other modules to react to. It does not depend on any other module.
 */
package com.williampower.budget.transaction;
