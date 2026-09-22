package com.yz.vault.domain.identifier;

import java.util.Objects;

/** A stable correlation identifier that is safe to expose in API responses and audit events. */
public record RequestId(String value) {

    public RequestId {
        value = requireValue(value, "requestId");
    }

    public static RequestId of(String value) {
        return new RequestId(value);
    }

    static String requireValue(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
