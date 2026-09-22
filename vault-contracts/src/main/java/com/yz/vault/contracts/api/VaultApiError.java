package com.yz.vault.contracts.api;

import java.util.Objects;

/** A client-safe error payload. Callers must not place secrets or internal stack details in its message. */
public record VaultApiError(VaultErrorCode code, String message) {

    public VaultApiError {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(message, "message must not be null");
        if (message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
    }
}
