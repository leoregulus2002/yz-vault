package com.yz.vault.domain.lease;

import com.yz.vault.domain.identifier.LeaseId;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/** Models the validity window of an issued secret without retaining the secret itself. */
public final class VaultLease {

    private final LeaseId id;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private Instant revokedAt;

    private VaultLease(LeaseId id, Instant issuedAt, Instant expiresAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.issuedAt = Objects.requireNonNull(issuedAt, "issuedAt must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
    }

    public static VaultLease issue(LeaseId id, Instant issuedAt, Duration ttl) {
        Objects.requireNonNull(ttl, "ttl must not be null");
        if (ttl.isZero() || ttl.isNegative()) {
            throw new IllegalArgumentException("ttl must be positive");
        }
        return new VaultLease(id, issuedAt, issuedAt.plus(ttl));
    }

    public boolean isActiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        if (instant.isBefore(issuedAt) || !instant.isBefore(expiresAt)) {
            return false;
        }
        return revokedAt == null || instant.isBefore(revokedAt);
    }

    public void revoke(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        if (instant.isBefore(issuedAt)) {
            throw new IllegalArgumentException("revocation time must not precede issuance");
        }
        if (revokedAt == null) {
            revokedAt = instant;
        }
    }
}
