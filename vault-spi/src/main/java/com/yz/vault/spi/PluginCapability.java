package com.yz.vault.spi;

/** Declares an operation a plugin implementation can safely provide. */
public enum PluginCapability {
    READ_SECRET,
    WRITE_SECRET,
    ENCRYPT,
    DECRYPT,
    PERSIST,
    AUTHENTICATE,
    ISSUE_DATABASE_CREDENTIAL,
    EMIT_AUDIT_EVENT
}
