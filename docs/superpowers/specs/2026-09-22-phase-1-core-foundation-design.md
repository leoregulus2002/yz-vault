# Phase 1 core-foundation completion design

## Purpose

Complete the unfulfilled Phase 1 architectural primitives without adding any
business capability from crypto, persistence, authentication, policy, secret
engines, or leases.  The result must preserve the existing modular-monolith
dependency direction and give subsequent phases explicit, testable seams.

## Scope

The Phase 1 baseline already provides domain identifiers, the lease value
model, API envelopes and error codes, plugin SPIs, a typed application
operation boundary, and the server bootstrap.  This completion adds only the
following missing primitives:

- a domain `BaseEntity<ID>` for identity-based equality and immutable entity
  identity;
- a domain-owned `VaultErrorCode` value type, consumed by API contracts rather
  than owned by the outward contracts module;
- domain `DomainException` and application `ApplicationException`, each
  carrying an explicit stable error code and safe message;
- an infrastructure-only `InfrastructureException` for adapter failures;
- a dependency-clean `vault-infrastructure` Maven module that contains the
  Phase 1 adapter implementations but no database, migration, jOOQ,
  PostgreSQL, connection-pool, or Testcontainers dependency;
- application repository ports that do not expose SQL, JDBC, jOOQ records, or
  framework annotations;
- domain ports for time, identifier creation, and secure randomness;
- infrastructure implementations backed by `Clock`, an injected ID factory,
  and `SecureRandom`.

No database schema, Flyway migration, jOOQ repository, encryption algorithm,
or authentication mechanism is part of this work.

## Module boundaries

```text
vault-server -> vault-infrastructure -> vault-application -> vault-domain
                                      -> vault-contracts / vault-spi
```

`vault-domain` remains framework- and persistence-free.  It owns the base
entity type, typed clock/ID/randomness ports, and domain failure semantics.
`vault-application` owns the repository-port vocabulary and use-case failure
semantics.  `vault-infrastructure` depends inward and supplies implementations
for those ports; its generic implementations do not select a database or add
any schema.  `vault-server` remains a bootstrap adapter and will wire concrete
implementations only when an approved later phase requires them.

The uncommitted infrastructure module in the original checkout is split by
intent: its module boundary is incorporated here, while its jOOQ, Flyway,
PostgreSQL, and Testcontainers dependencies remain Phase 2 work and are not
copied into this Phase 1 implementation.

## Contracts

### Base entity

`BaseEntity<ID>` requires a non-null immutable identifier.  Equality and hash
code use the concrete runtime type and identifier so two different domain
types cannot compare equal merely because their identifier values match.

### Failures

Each layer-specific exception exposes a stable domain-owned `VaultErrorCode`
and only a caller-provided safe message.  This ownership keeps the domain from
depending outward on API contracts.  Exceptions do not capture or format
secret values, credentials, SQL, or implementation internals.  Infrastructure
failures can retain a cause for server-side diagnostics while exposing a
generic stable code to the application boundary.

### Ports

The clock port returns an `Instant`.  The identifier port produces typed
`RequestId` values.  The secure-random port fills caller-supplied byte arrays
and rejects invalid input.  The generic repository port offers lookup, save,
and deletion by typed identifier without defining persistence behavior for any
future aggregate.  Domain and application code may depend on these contracts;
they never depend on their infrastructure implementations.

## Error handling and security

The secure-random adapter uses `java.security.SecureRandom`, never
`java.util.Random`, UUID values as secrets, or mutable global state.  The
clock adapter accepts a `java.time.Clock` dependency, making tests
deterministic.  IDs use an injected non-secret string supplier to keep the
domain deterministic and prevent a future generator from accidentally being
treated as credential generation.  Exceptions and Javadocs must not include
sensitive payloads.

## Testing

Tests are written first for each new behavior.  They prove entity identity
semantics, exception metadata, repository-port shape through a small in-memory
test implementation, fixed-clock behavior, input validation, typed ID
creation, and secure-random byte filling.  Tests remain local unit tests;
Testcontainers and database integration belong to a persistence phase.

## Acceptance criteria

1. Each Phase 1 primitive named in the product specification exists in the
   appropriate layer.
2. Neither `vault-domain` nor `vault-application` imports infrastructure,
   JDBC, jOOQ, PostgreSQL, Spring, or SQL types.
3. No feature from later phases is implemented.
4. The existing working-tree changes for Phase 2 are absent from this
   isolated workspace and remain untouched in the original checkout.
5. The Phase 1 design, architecture documentation, development status,
   decisions record, changelog, and an ADR accurately record the completed
   baseline.
