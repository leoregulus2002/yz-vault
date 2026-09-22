package com.yz.vault.contracts.request;

import com.yz.vault.domain.identifier.RequestId;
import java.time.Instant;
import java.util.Objects;

/** Metadata propagated with an operation; it must never contain credentials or secret values. */
public record RequestContext(RequestId requestId, Instant requestedAt) {

    public RequestContext {
        Objects.requireNonNull(requestId, "requestId must not be null");
        Objects.requireNonNull(requestedAt, "requestedAt must not be null");
    }
}
