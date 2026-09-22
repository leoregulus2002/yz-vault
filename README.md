# yz-vault

`yz-vault` is an independently designed enterprise secrets and key-management platform. It is inspired by established secrets-management concepts, but does not copy HashiCorp Vault source code.

## Phase 1 status

This repository currently provides a Java 26 / Spring Boot 4 architecture skeleton. It intentionally contains no secret storage, encryption implementation, authentication method, database integration, or HTTP business API. Those capabilities are delivered only in their approved phases.

## Modules

| Module | Responsibility |
| --- | --- |
| `vault-domain` | Framework-free identifiers and Lease lifecycle model. |
| `vault-contracts` | API response envelope, error codes, and request metadata. |
| `vault-spi` | Stable plugin contracts for secret engines, cryptography, storage, authentication, database plugins, and audit sinks. |
| `vault-application` | Typed use-case boundary between adapters and domain/SPI contracts. |
| `vault-server` | Independently runnable Spring Boot server bootstrap. |

## Build

Use Java 26 and Maven:

```bash
mvn clean verify
```

## Security baseline

- Never commit real credentials, keys, tokens, certificates, or `.env` files.
- The repository configures `.githooks/pre-commit` through local Git configuration to reject common sensitive filenames and plaintext credential signatures. After a fresh clone, run `git config core.hooksPath .githooks` before committing.
- The hook is local defense in depth, not a replacement for CI or server-side secret scanning.
- Audit contracts intentionally exclude secret payloads, credentials, and key material.

## Architecture

See [architecture.md](docs/architecture.md) and [ADR-0001](docs/adr/0001-modular-monolith-and-dependency-rule.md).
