package com.yz.vault.application.error;

import com.yz.vault.domain.error.VaultErrorCode;
import java.util.Objects;

/** Signals an application use-case failure using a stable code and client-safe message. */
public class ApplicationException extends RuntimeException {

    private final VaultErrorCode code;

    public ApplicationException(VaultErrorCode code, String safeMessage) {
        super(Objects.requireNonNull(safeMessage, "safeMessage must not be null"));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }

    public VaultErrorCode code() {
        return code;
    }
}
