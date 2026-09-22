# yz-vault architecture

## Scope

Phase 1 establishes a modular monolith. The server is a single independently deployable Spring Boot application while its core has strict module boundaries. It defines technology-neutral foundation contracts but does not define persistence, crypto, authentication, or externally consumable secret behavior.

## Dependency direction

```text
vault-server → vault-application → vault-spi → vault-contracts → vault-domain
                                 ↘ vault-domain
vault-infrastructure → vault-domain
```

`vault-contracts` uses `RequestId` from `vault-domain` so the API correlation identifier is one stable value object across HTTP, application, and audit boundaries. `vault-domain` has no dependency on Spring, database drivers, MyBatis, Flyway, or a plugin implementation.

## Module boundaries

### vault-domain

Contains the framework-free ubiquitous language: `RequestId`, `LeaseId`, `KeyVersionId`, and `VaultLease`. A lease only contains identity and validity metadata; it never retains the associated secret.

### vault-contracts

Defines `RequestContext`, the `VaultApiResponse` envelope, and machine-readable `VaultErrorCode` values. API errors must be client-safe and never contain keys, tokens, credentials, or stack traces.

### vault-spi

Defines the plugin contracts requested by the product: `SecretEngine`, `CryptoProvider`, `Storage`, `AuthProvider`, `DatabasePlugin`, and `AuditSink`. The first five expose a generic, type-safe `execute(PluginOperation<R>)` boundary; each operation declares its target plugin kind and required capability. This establishes replaceable execution without prematurely defining how secret payloads are represented. Each plugin also exposes immutable non-sensitive metadata through `PluginDescriptor`; no infrastructure implementation is selected at this layer.

### vault-application

Defines the typed `VaultOperation` use-case boundary, application failures, and persistence-neutral repository ports. Future command/query handlers coordinate domain behavior and SPI implementations here.

### vault-infrastructure

Contains generic adapters for time, identifiers, and cryptographically secure random bytes. It uses injected JDK types and deliberately has no jOOQ, Flyway, PostgreSQL, HikariCP, or Testcontainers dependency in Phase 1.

### vault-server

Contains only Spring Boot bootstrapping at this stage. Spring Security HTTP integration will be added when an approved authentication and policy phase defines the actual security model; it will not replace Vault policy evaluation.

## Security invariants

1. Domain and API DTOs must not rely on persistence or web frameworks.
2. Secret values must not enter API error messages or audit events.
3. Secret engines, crypto providers, storage, authentication methods, database plugins, and audit sinks remain replaceable through SPI contracts.
4. All future persistence adapters sit outside `vault-domain`.

## Deferred work

The next approved phases add persistence, storage initialization, cryptography, seal/unseal, authentication, policies, and leases incrementally. None is implied by this skeleton.
