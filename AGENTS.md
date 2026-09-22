# yz-vault Agent Guide

## Delivery protocol

- Work on one approved phase at a time and stop after its completion report.
- Keep dependencies directed inward: adapters depend on application/domain contracts; domain and application never depend on infrastructure technology.
- Never persist or log plaintext secrets, tokens, passwords, or key material.
- Add an ADR for durable architecture decisions and update `CHANGELOG.md` for delivered behavior.

## Current handoff

- Phase 0 and Phase 1 are complete.
- Phase 2 is **Crypto & Key Management**. It may implement envelope encryption, AES-256-GCM, key versions, key rotation, and `MasterKeyProvider`; it must not introduce unrelated authentication, policy, secret-engine, or database-persistence behavior.
