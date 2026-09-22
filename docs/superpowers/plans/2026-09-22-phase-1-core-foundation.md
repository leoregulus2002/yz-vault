# Phase 1 Core Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Complete the missing Phase 1 architectural primitives without adding crypto, persistence, authentication, policy, secret-engine, or lease business behavior.

**Architecture:** Move stable error-code ownership into `vault-domain`. Domain owns entities and framework-free ports, application owns repository contracts, and a clean infrastructure module supplies generic time, identifier, and secure-random adapters.

**Tech Stack:** Java 26, Maven multi-module build, JUnit 5, `java.time.Clock`, `java.security.SecureRandom`.

**Spec:** `docs/superpowers/specs/2026-09-22-phase-1-core-foundation-design.md`

## Global Constraints

- Preserve inward-only dependencies: domain and application may not import infrastructure, Spring, JDBC, jOOQ, SQL, PostgreSQL, Flyway, or Testcontainers.
- Add only Phase 1 primitives; add no schema, migration, jOOQ repository, encryption, authentication, policy, secret-engine, or lease business behavior.
- Use `SecureRandom` for random bytes; never use `Random`, `Math.random`, or UUID values as secrets.
- Stable errors use codes including `YZV-100001`, `YZV-200001`, and `YZV-300001`; API errors and exceptions must not add sensitive data to messages.
- Follow red-green-refactor for every new production behavior.
- Do not copy the original checkout’s uncommitted jOOQ/Flyway/PostgreSQL/Testcontainers work into this worktree.

## Review Focus

- Two aggregate classes with the same identifier must never compare equal; Task 1 has sibling-entity coverage.
- A domain exception must retain its stable code and safe message; Task 1 covers both.
- A repository caller must distinguish a missing entity from a stored `null`; Task 2 uses `Optional.empty()`.
- The clock adapter must use an injected fixed clock, not the machine clock; Task 3 asserts an exact instant.
- The random adapter must reject a null buffer and fill a supplied non-empty buffer; Task 3 covers both inputs.

---

### Task 1: Establish domain error ownership and entity identity semantics

**Files:**
- Create: `vault-domain/src/main/java/com/yz/vault/domain/error/VaultErrorCode.java`
- Create: `vault-domain/src/main/java/com/yz/vault/domain/error/DomainException.java`
- Create: `vault-domain/src/main/java/com/yz/vault/domain/model/BaseEntity.java`
- Create: `vault-domain/src/test/java/com/yz/vault/domain/error/DomainExceptionTest.java`
- Create: `vault-domain/src/test/java/com/yz/vault/domain/model/BaseEntityTest.java`
- Delete: `vault-contracts/src/main/java/com/yz/vault/contracts/api/VaultErrorCode.java`
- Modify: `vault-contracts/src/main/java/com/yz/vault/contracts/api/VaultApiError.java`
- Modify: `vault-contracts/src/test/java/com/yz/vault/contracts/api/VaultApiResponseTest.java`

**Interfaces:**
- Produces: `VaultErrorCode.value(): String`, `DomainException.code(): VaultErrorCode`, `BaseEntity<ID>.id(): ID`.
- Consumes: `VaultApiError(VaultErrorCode code, String message)` imports the domain-owned error code.

- [ ] **Step 1: Write the failing domain tests**

```java
@Test
void retainsStableCodeAndSafeMessage() {
    var exception = new DomainException(VaultErrorCode.DOMAIN_INVARIANT_VIOLATION, "lease id is invalid");
    assertEquals("YZV-100001", exception.code().value());
    assertEquals("lease id is invalid", exception.getMessage());
}

@Test
void comparesOnlyEntitiesOfTheSameConcreteTypeAndIdentifier() {
    assertEquals(new Alpha("id-1"), new Alpha("id-1"));
    assertNotEquals(new Alpha("id-1"), new Beta("id-1"));
}

private static final class Alpha extends BaseEntity<String> {
    private Alpha(String id) { super(id); }
}

private static final class Beta extends BaseEntity<String> {
    private Beta(String id) { super(id); }
}
```

- [ ] **Step 2: Run the tests to verify the intended failure**

Run: `mvn -pl vault-domain -Dtest=DomainExceptionTest,BaseEntityTest test`

Expected: FAIL because `VaultErrorCode`, `DomainException`, and `BaseEntity` do not exist.

- [ ] **Step 3: Write the minimal implementation**

```java
public enum VaultErrorCode {
    DOMAIN_INVARIANT_VIOLATION("YZV-100001"),
    APPLICATION_OPERATION_FAILED("YZV-200001"),
    INFRASTRUCTURE_FAILURE("YZV-300001"),
    INVALID_REQUEST("YZV-100002"),
    UNAUTHORIZED("YZV-100003"),
    FORBIDDEN("YZV-100004"),
    NOT_FOUND("YZV-100005"),
    CONFLICT("YZV-100006"),
    SEALED("YZV-100007"),
    INTERNAL_ERROR("YZV-300002");

    private final String value;
    VaultErrorCode(String value) { this.value = value; }
    public String value() { return value; }
}

public class DomainException extends RuntimeException {
    private final VaultErrorCode code;
    public DomainException(VaultErrorCode code, String safeMessage) {
        super(Objects.requireNonNull(safeMessage, "safeMessage must not be null"));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }
    public VaultErrorCode code() { return code; }
}

public abstract class BaseEntity<ID> {
    private final ID id;
    protected BaseEntity(ID id) { this.id = Objects.requireNonNull(id, "id must not be null"); }
    public final ID id() { return id; }
    @Override public final boolean equals(Object other) {
        return this == other || (other != null && getClass() == other.getClass()
                && id.equals(((BaseEntity<?>) other).id));
    }
    @Override public final int hashCode() { return 31 * getClass().hashCode() + id.hashCode(); }
}
```

Import `com.yz.vault.domain.error.VaultErrorCode` in `VaultApiError` and its test, then remove the contracts-owned enum.

- [ ] **Step 4: Run focused tests to verify green**

Run: `mvn -pl vault-domain test && mvn -pl vault-contracts -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=VaultApiResponseTest test`

Expected: PASS.

- [ ] **Step 5: Commit the completed domain foundation**

```bash
git add vault-domain vault-contracts
git commit -m "feat(core): add domain error and entity foundations"
```

### Task 2: Add domain utility ports and application repository/failure contracts

**Files:**
- Create: `vault-domain/src/main/java/com/yz/vault/domain/time/VaultClock.java`
- Create: `vault-domain/src/main/java/com/yz/vault/domain/identifier/IdentifierGenerator.java`
- Create: `vault-domain/src/main/java/com/yz/vault/domain/random/SecureRandomSource.java`
- Create: `vault-application/src/main/java/com/yz/vault/application/error/ApplicationException.java`
- Create: `vault-application/src/main/java/com/yz/vault/application/port/RepositoryPort.java`
- Create: `vault-application/src/test/java/com/yz/vault/application/error/ApplicationExceptionTest.java`
- Create: `vault-application/src/test/java/com/yz/vault/application/port/RepositoryPortTest.java`
- Modify: `vault-application/pom.xml`

**Interfaces:**
- Produces: `VaultClock.now(): Instant`, `IdentifierGenerator<T>.next(): T`, and `SecureRandomSource.nextBytes(byte[]): void`.
- Produces: `RepositoryPort<E, ID>.findById(ID): Optional<E>`, `save(E): E`, and `deleteById(ID): boolean`.
- Consumes: `ApplicationException(VaultErrorCode code, String safeMessage)` uses Task 1’s error code.

- [ ] **Step 1: Add JUnit test scope and write the failing application tests**

```java
@Test
void retainsApplicationErrorMetadata() {
    var exception = new ApplicationException(VaultErrorCode.APPLICATION_OPERATION_FAILED, "save was rejected");
    assertEquals("YZV-200001", exception.code().value());
    assertEquals("save was rejected", exception.getMessage());
}

@Test
void repositoryPortSupportsAnExplicitMissingResult() {
    RepositoryPort<String, String> repository = new InMemoryRepository();
    assertTrue(repository.findById("missing").isEmpty());
    assertEquals("value", repository.save("value"));
    assertTrue(repository.deleteById("value"));
}
```

Add `org.junit.jupiter:junit-jupiter` with test scope to `vault-application/pom.xml`. Add a private `InMemoryRepository` test class backed by `Map<String, String>` and using the value as its key.

- [ ] **Step 2: Run the tests to verify the intended failure**

Run: `mvn -pl vault-application -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=ApplicationExceptionTest,RepositoryPortTest test`

Expected: FAIL because the application exception and repository port do not exist.

- [ ] **Step 3: Write the minimal interfaces and application exception**

```java
@FunctionalInterface
public interface VaultClock { Instant now(); }

@FunctionalInterface
public interface IdentifierGenerator<T> { T next(); }

@FunctionalInterface
public interface SecureRandomSource { void nextBytes(byte[] destination); }

public interface RepositoryPort<E, ID> {
    Optional<E> findById(ID id);
    E save(E entity);
    boolean deleteById(ID id);
}

public class ApplicationException extends RuntimeException {
    private final VaultErrorCode code;
    public ApplicationException(VaultErrorCode code, String safeMessage) {
        super(Objects.requireNonNull(safeMessage, "safeMessage must not be null"));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }
    public VaultErrorCode code() { return code; }
}
```

- [ ] **Step 4: Run focused tests to verify green**

Run: `mvn -pl vault-application -am -Dtest=ApplicationExceptionTest,RepositoryPortTest test`

Expected: PASS.

- [ ] **Step 5: Commit ports and application error semantics**

```bash
git add vault-domain vault-application
git commit -m "feat(core): add application ports and utility contracts"
```

### Task 3: Add the clean infrastructure module and generic adapters

**Files:**
- Modify: `pom.xml`
- Create: `vault-infrastructure/pom.xml`
- Create: `vault-infrastructure/src/main/java/com/yz/vault/infrastructure/error/InfrastructureException.java`
- Create: `vault-infrastructure/src/main/java/com/yz/vault/infrastructure/time/ClockVaultClock.java`
- Create: `vault-infrastructure/src/main/java/com/yz/vault/infrastructure/identifier/SupplierIdentifierGenerator.java`
- Create: `vault-infrastructure/src/main/java/com/yz/vault/infrastructure/random/JavaSecureRandomSource.java`
- Create: `vault-infrastructure/src/test/java/com/yz/vault/infrastructure/error/InfrastructureExceptionTest.java`
- Create: `vault-infrastructure/src/test/java/com/yz/vault/infrastructure/time/ClockVaultClockTest.java`
- Create: `vault-infrastructure/src/test/java/com/yz/vault/infrastructure/identifier/SupplierIdentifierGeneratorTest.java`
- Create: `vault-infrastructure/src/test/java/com/yz/vault/infrastructure/random/JavaSecureRandomSourceTest.java`

**Interfaces:**
- Consumes: Task 1 `VaultErrorCode`; Task 2 `VaultClock`, `IdentifierGenerator<T>`, and `SecureRandomSource`.
- Produces: `ClockVaultClock(Clock).now(): Instant`, `SupplierIdentifierGenerator<T>(Supplier<T>).next(): T`, and `JavaSecureRandomSource(SecureRandom).nextBytes(byte[]): void`.

- [ ] **Step 1: Add test-only module configuration and write failing adapter tests**

Add `vault-infrastructure` to the root modules and create a POM with only `vault-domain` plus JUnit 5 test scope. Do not copy jOOQ, Flyway, PostgreSQL, Spring, HikariCP, or Testcontainers dependencies.

```java
@Test
void readsTheInjectedFixedClock() {
    var clock = new ClockVaultClock(Clock.fixed(Instant.parse("2026-09-22T00:00:00Z"), ZoneOffset.UTC));
    assertEquals(Instant.parse("2026-09-22T00:00:00Z"), clock.now());
}

@Test
void fillsTheProvidedBufferAndRejectsNull() {
    var source = new JavaSecureRandomSource(new DeterministicSecureRandom());
    var bytes = new byte[32];
    source.nextBytes(bytes);
    assertThrows(NullPointerException.class, () -> source.nextBytes(null));
    assertArrayEquals(new byte[] {0x5a, 0x5a, 0x5a, 0x5a}, Arrays.copyOf(bytes, 4));
}

@Test
void returnsValuesFromTheInjectedSupplier() {
    var generator = new SupplierIdentifierGenerator<>(() -> RequestId.of("req-1"));
    assertEquals(RequestId.of("req-1"), generator.next());
}
```

Add a private `DeterministicSecureRandom extends SecureRandom` whose `nextBytes` fills the supplied array with `(byte) 0x5a`. Also assert that `InfrastructureException` retains `YZV-300001`, its safe message, and its supplied cause.

- [ ] **Step 2: Run the tests to verify the intended failure**

Run: `mvn -pl vault-infrastructure -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=InfrastructureExceptionTest,ClockVaultClockTest,SupplierIdentifierGeneratorTest,JavaSecureRandomSourceTest test`

Expected: FAIL because the infrastructure adapters and exception do not exist.

- [ ] **Step 3: Write the minimal adapters**

```java
public final class ClockVaultClock implements VaultClock {
    private final Clock clock;
    public ClockVaultClock(Clock clock) { this.clock = Objects.requireNonNull(clock, "clock must not be null"); }
    @Override public Instant now() { return clock.instant(); }
}

public final class SupplierIdentifierGenerator<T> implements IdentifierGenerator<T> {
    private final Supplier<T> supplier;
    public SupplierIdentifierGenerator(Supplier<T> supplier) { this.supplier = Objects.requireNonNull(supplier, "supplier must not be null"); }
    @Override public T next() { return Objects.requireNonNull(supplier.get(), "generated identifier must not be null"); }
}

public final class JavaSecureRandomSource implements SecureRandomSource {
    private final SecureRandom secureRandom;
    public JavaSecureRandomSource(SecureRandom secureRandom) { this.secureRandom = Objects.requireNonNull(secureRandom, "secureRandom must not be null"); }
    @Override public void nextBytes(byte[] destination) { secureRandom.nextBytes(Objects.requireNonNull(destination, "destination must not be null")); }
}
```

Implement `InfrastructureException` with a non-null `VaultErrorCode`, safe message, and optional cause passed to `RuntimeException`.

```java
public class InfrastructureException extends RuntimeException {
    private final VaultErrorCode code;
    public InfrastructureException(VaultErrorCode code, String safeMessage, Throwable cause) {
        super(Objects.requireNonNull(safeMessage, "safeMessage must not be null"), cause);
        this.code = Objects.requireNonNull(code, "code must not be null");
    }
    public VaultErrorCode code() { return code; }
}
```

- [ ] **Step 4: Run focused tests to verify green**

Run: `mvn -pl vault-infrastructure -am -Dtest=InfrastructureExceptionTest,ClockVaultClockTest,SupplierIdentifierGeneratorTest,JavaSecureRandomSourceTest test`

Expected: PASS.

- [ ] **Step 5: Commit the clean infrastructure boundary**

```bash
git add pom.xml vault-infrastructure
git commit -m "feat(core): add infrastructure foundation adapters"
```

### Task 4: Synchronize Phase 1 documents and verify the completion record

**Files:**
- Create: `AGENTS.md`
- Create: `docs/development-status.md`
- Create: `docs/decisions.md`
- Create: `docs/project-roadmap.md`
- Create: `docs/adr/0002-core-foundation-primitives.md`
- Create: `docs/superpowers/checks/phase-1-docs.sh`
- Modify: `README.md`
- Modify: `CHANGELOG.md`
- Modify: `docs/architecture.md`

**Interfaces:**
- Consumes: implemented module names and contracts from Tasks 1–3.
- Produces: an accurate Phase 1 completion record and a Phase 2 handoff to crypto/key management only.

- [ ] **Step 1: Write the failing documentation checks**

Create `docs/superpowers/checks/phase-1-docs.sh` with:

```bash
#!/usr/bin/env bash
set -euo pipefail
rg -q 'Phase 1.*COMPLETED' docs/development-status.md
rg -q 'Phase 2.*Crypto' docs/development-status.md
rg -q 'vault-infrastructure' README.md docs/architecture.md
rg -q 'SecureRandom' docs/adr/0002-core-foundation-primitives.md
! rg -q 'jOOQ|Flyway|PostgreSQL|Testcontainers|HikariCP' vault-infrastructure/pom.xml
```

- [ ] **Step 2: Run documentation checks to verify the intended failure**

Run: `bash docs/superpowers/checks/phase-1-docs.sh`

Expected: FAIL because the completion and decision documents do not exist and README does not list `vault-infrastructure`.

- [ ] **Step 3: Write the completion record and durable decisions**

Set Phase 1 to `COMPLETED` in `docs/development-status.md`. List the domain-owned error code, base entity, utility ports, repository port, and clean infrastructure adapters. Set the next phase to `Phase 2 — Crypto & Key Management` without adding its implementation. Update README and architecture documentation to list `vault-infrastructure` and its no-database boundary. Create ADR-0002 explaining domain error-code ownership and injected time/ID/randomness adapters. Add a changelog entry. `AGENTS.md`, `docs/decisions.md`, and the roadmap must use the same Phase 2 handoff and must not declare database persistence active.

- [ ] **Step 4: Run documentation checks and the complete Maven test suite**

Run: `bash docs/superpowers/checks/phase-1-docs.sh && mvn test`

Expected: document checks PASS and all Maven modules PASS.

- [ ] **Step 5: Commit the Phase 1 completion record**

```bash
git add AGENTS.md README.md CHANGELOG.md docs
git commit -m "docs(phase1): record core foundation completion"
```

### Task 5: Verify boundaries and prepare the Phase 1 delivery report

**Files:**
- Modify: `docs/development-status.md` only when final verification introduces a result that must be recorded.

**Interfaces:**
- Consumes: all Tasks 1–4.
- Produces: a clean, committed Phase 1 completion branch with evidence for the user-facing completion report.

- [ ] **Step 1: Verify dependency boundaries with source scans**

Run:

```bash
! rg -n 'org\.jooq|java\.sql|org\.postgresql|org\.springframework' vault-domain/src vault-application/src
! rg -n 'jooq|flyway|postgresql|testcontainers|hikari' vault-infrastructure/pom.xml
git status --short
```

Expected: both scans produce no matches and the worktree is clean.

- [ ] **Step 2: Run the full Phase 1 test suite**

Run: `mvn test`

Expected: PASS for domain, contracts, SPI, application, infrastructure, and server tests.

- [ ] **Step 3: Inspect committed changes before delivery**

Run:

```bash
git log --oneline 667b450..HEAD
git diff --check 667b450..HEAD
git status --short
```

Expected: only Phase 1 foundation and documentation commits, no whitespace errors, and a clean worktree.

- [ ] **Step 4: Commit verification evidence only if the status file changed**

```bash
git add docs/development-status.md
git commit -m "docs(phase1): record verification evidence"
```

If no file changed during verification, do not create an empty commit.
