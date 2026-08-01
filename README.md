# Personal Budget (Spring Modulith)

A simple personal budget app built as a **modular monolith** with [Spring Modulith](https://spring.io/projects/spring-modulith). It records income and expenses, tracks per-category budgets, and maintains a running balance — with the three modules talking to each other **only via Spring's application event bus** (`ApplicationEventPublisher` / `@ApplicationModuleListener`), never through direct method calls.

## Stack

- Spring Boot 3.5.13, Java 21, Gradle (wrapper included)
- Spring Modulith 1.4.12 — module boundaries, event publication registry, `/actuator/modulith`
- Spring Data JDBC — no JPA/Hibernate, plain SQL mapping
- PostgreSQL, initialized via `schema.sql` (`spring.sql.init.mode=always`)
- Spring Boot Actuator
- Testcontainers + Spring Modulith's `@ApplicationModuleTest` / `Scenario` API for integration tests

## Modules

| Module | Responsibility | Publishes | Listens to |
|---|---|---|---|
| `transaction` | Records income/expense entries (source of truth) | `TransactionRecorded` | — |
| `budget` | Tracks per-category monthly limits and spend | `BudgetExceeded` | `TransactionRecorded` |
| `summary` | Maintains running account balance and monthly totals | — | `TransactionRecorded` |

Each module exposes only what it wants other modules to see (event types, occasionally nothing) at its package root; everything else — entities, repositories, services, controllers — lives in an `internal` subpackage. Spring Modulith enforces this boundary at the package level regardless of Java visibility modifiers, and `ModularityTests` (in `src/test`) runs `ApplicationModules.verify()` to fail the build if a module ever reaches into another module's internals.

You can see the enforced dependency graph at runtime via `GET /actuator/modulith` — `budget` and `summary` show up depending on `transaction` only through an `EVENT_LISTENER` relationship, never a direct type reference.

## Running locally

Requires Docker (used both for local development and for tests).

```bash
./gradlew bootRun
```

Spring Boot's Docker Compose support reads `compose.yaml` and starts a Postgres container automatically, wiring the datasource for you — no manual setup needed. The app comes up on `http://localhost:8080`.

## API

**Transactions**
- `POST /api/transactions` — `{"type":"INCOME"|"EXPENSE","amount":100.00,"category":"groceries","description":"...","occurredOn":"2026-08-01"}`
- `GET /api/transactions`

**Budgets**
- `POST /api/budgets` — `{"category":"groceries","monthlyLimit":300.00}`
- `GET /api/budgets`
- `GET /api/budgets/{category}/status?yearMonth=2026-08`

**Summary**
- `GET /api/summary/balance`
- `GET /api/summary/monthly/{yearMonth}` (e.g. `2026-08`)

**Actuator**
- `GET /actuator/health`, `/actuator/info`, `/actuator/metrics`, `/actuator/modulith`

## Tests

```bash
./gradlew test
```

This spins up Postgres via Testcontainers and runs:
- `ModularityTests` — verifies the module structure (no illegal cross-module access)
- `TransactionModuleTests`, `BudgetModuleTests`, `SummaryModuleTests` — `@ApplicationModuleTest`s that publish/await real events end-to-end using Spring Modulith's `Scenario` API, so the async event flow between modules is actually exercised, not mocked
- `BudgetApplicationTests` — full context load smoke test
