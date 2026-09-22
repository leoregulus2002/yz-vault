package com.yz.vault.domain.lease;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.yz.vault.domain.identifier.LeaseId;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class VaultLeaseTest {

    @Test
    void isActiveOnlyBeforeItsExpiry() {
        var issuedAt = Instant.parse("2026-09-22T00:00:00Z");
        var lease = VaultLease.issue(LeaseId.of("lease-1"), issuedAt, Duration.ofMinutes(5));

        assertTrue(lease.isActiveAt(Instant.parse("2026-09-22T00:04:59Z")));
        assertFalse(lease.isActiveAt(Instant.parse("2026-09-22T00:05:00Z")));
    }

    @Test
    void revokeMakesAnOtherwiseValidLeaseInactive() {
        var issuedAt = Instant.parse("2026-09-22T00:00:00Z");
        var lease = VaultLease.issue(LeaseId.of("lease-2"), issuedAt, Duration.ofMinutes(5));

        lease.revoke(Instant.parse("2026-09-22T00:01:00Z"));

        assertFalse(lease.isActiveAt(Instant.parse("2026-09-22T00:01:01Z")));
    }

    @Test
    void preservesHistoricalValidityUntilTheRevocationInstant() {
        var issuedAt = Instant.parse("2026-09-22T00:00:00Z");
        var lease = VaultLease.issue(LeaseId.of("lease-3"), issuedAt, Duration.ofMinutes(5));

        lease.revoke(Instant.parse("2026-09-22T00:04:00Z"));

        assertTrue(lease.isActiveAt(Instant.parse("2026-09-22T00:02:00Z")));
        assertFalse(lease.isActiveAt(Instant.parse("2026-09-22T00:04:00Z")));
    }

    @Test
    void rejectsRevocationBeforeTheLeaseWasIssued() {
        var issuedAt = Instant.parse("2026-09-22T00:00:00Z");
        var lease = VaultLease.issue(LeaseId.of("lease-4"), issuedAt, Duration.ofMinutes(5));

        assertThrows(
                IllegalArgumentException.class,
                () -> lease.revoke(Instant.parse("2026-09-21T23:59:59Z")));
    }
}
