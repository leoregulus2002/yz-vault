package com.yz.vault.spi;

import com.yz.vault.domain.identifier.RequestId;
import java.time.Instant;
import java.util.Objects;

/** A secret-free audit record. Payloads, credentials, and key material are deliberately excluded. */
public record AuditEvent(RequestId requestId, String action, boolean successful, Instant occurredAt) {

    public AuditEvent {
        Objects.requireNonNull(requestId, "requestId must not be null");
        Objects.requireNonNull(action, "action must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
        if (action.isBlank()) {
            throw new IllegalArgumentException("action must not be blank");
        }
    }
}
