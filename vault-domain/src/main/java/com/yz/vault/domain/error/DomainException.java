package com.yz.vault.domain.error;

import java.util.Objects;

/** Signals a domain invariant violation using a client-safe message and stable error code. */
public class DomainException extends RuntimeException {

    private final VaultErrorCode code;

    public DomainException(VaultErrorCode code, String safeMessage) {
        super(Objects.requireNonNull(safeMessage, "safeMessage must not be null"));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }

    public VaultErrorCode code() {
        return code;
    }
}
