package com.yz.vault.contracts.api;

/** Stable machine-readable error categories exposed by the yz-vault HTTP API. */
public enum VaultErrorCode {
    INVALID_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    CONFLICT,
    SEALED,
    INTERNAL_ERROR
}
