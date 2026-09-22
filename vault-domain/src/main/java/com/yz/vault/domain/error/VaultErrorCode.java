package com.yz.vault.domain.error;

/** Stable, non-sensitive error codes shared across yz-vault boundaries. */
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

    VaultErrorCode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
