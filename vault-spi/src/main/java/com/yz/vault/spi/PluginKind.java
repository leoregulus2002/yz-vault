package com.yz.vault.spi;

/** Extension categories recognized by the yz-vault application boundary. */
public enum PluginKind {
    SECRET_ENGINE,
    CRYPTO_PROVIDER,
    STORAGE,
    AUTH_PROVIDER,
    DATABASE_PLUGIN,
    AUDIT_SINK
}
