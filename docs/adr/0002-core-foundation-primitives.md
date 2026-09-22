# ADR-0002: Keep core primitives technology-neutral

- Status: Accepted
- Date: 2026-09-22

## Context

Phase 1 needs stable errors, identity semantics, repository vocabulary, time, ID, and secure random sources without binding future use cases to Spring, SQL, or a specific runtime implementation.

## Decision

`VaultErrorCode` is owned by `vault-domain`; contracts and outer layers consume it. Domain exposes `VaultClock`, `IdentifierGenerator`, and `SecureRandomSource` ports. `vault-infrastructure` implements those ports with injected `Clock`, `Supplier`, and `SecureRandom`. `RepositoryPort` belongs to `vault-application` and exposes no persistence technology.

## Consequences

Tests remain deterministic, production random bytes use `SecureRandom`, and future adapters can change without reversing dependencies. Database libraries are deliberately absent from this Phase 1 module.
