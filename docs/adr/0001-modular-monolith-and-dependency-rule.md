# ADR-0001: Start with a modular monolith and inward-only dependencies

- Status: Accepted
- Date: 2026-09-22

## Context

yz-vault must evolve into a deployable secrets and key-management platform without coupling its security core to a specific database, crypto provider, authentication method, or web framework. The first deployment model is one Vault server plus PostgreSQL, with optional Agent support later. Splitting the system into many services before the core contracts stabilize would add operational risk without delivering security value.

## Decision

Build one independently deployable `vault-server` in a Maven multi-module repository. Keep domain behavior in `vault-domain`, cross-boundary API contracts in `vault-contracts`, replaceable capability contracts in `vault-spi`, use-case coordination in `vault-application`, and Spring Boot adapters in `vault-server`.

Dependencies point inward. `vault-domain` must not depend on Spring Boot, Spring Security, MyBatis-Plus, MyBatis, Flyway, JDBC drivers, or any selected storage implementation. `vault-contracts` may use domain value objects only for stable correlation semantics. Infrastructure implementations must depend on, never be depended on by, the core contracts.

## Consequences

Future persistence and plugin choices can change without structural changes to the domain model. The cost is more modules and explicit mapping at module boundaries. A future move to multiple deployables remains possible, but only after measured operational and security requirements justify it.
