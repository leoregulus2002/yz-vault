package com.yz.vault.infrastructure.error;

import com.yz.vault.domain.error.VaultErrorCode;
import java.util.Objects;

/** Signals an adapter failure with a stable code and client-safe message. */
public class InfrastructureException extends RuntimeException {

    private final VaultErrorCode code;

    public InfrastructureException(VaultErrorCode code, String safeMessage, Throwable cause) {
        super(Objects.requireNonNull(safeMessage, "safeMessage must not be null"), cause);
        this.code = Objects.requireNonNull(code, "code must not be null");
    }

    public VaultErrorCode code() {
        return code;
    }
}
