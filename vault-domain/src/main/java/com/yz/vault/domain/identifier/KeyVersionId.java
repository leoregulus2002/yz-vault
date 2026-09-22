package com.yz.vault.domain.identifier;

/** Identifies one immutable version of a cryptographic key. */
public record KeyVersionId(String value) {

    public KeyVersionId {
        value = RequestId.requireValue(value, "keyVersionId");
    }

    public static KeyVersionId of(String value) {
        return new KeyVersionId(value);
    }
}
