#!/usr/bin/env bash
set -euo pipefail

rg -q '^Phase 1' docs/development-status.md
rg -q '^COMPLETED$' docs/development-status.md
rg -q 'Phase 2.*Crypto' docs/development-status.md
rg -q 'vault-infrastructure' README.md docs/architecture.md
rg -q 'SecureRandom' docs/adr/0002-core-foundation-primitives.md
! rg -q 'jOOQ|Flyway|PostgreSQL|Testcontainers|HikariCP' vault-infrastructure/pom.xml
