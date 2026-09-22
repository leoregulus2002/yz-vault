package com.yz.vault.contracts.api;

import com.yz.vault.contracts.request.RequestContext;
import java.util.Objects;

/** Envelope shared by future HTTP endpoints; it separates successful data from client-safe errors. */
public record VaultApiResponse<T>(boolean success, String requestId, T data, VaultApiError error) {

    public VaultApiResponse {
        Objects.requireNonNull(requestId, "requestId must not be null");
        if (requestId.isBlank()) {
            throw new IllegalArgumentException("requestId must not be blank");
        }
        if (success && error != null) {
            throw new IllegalArgumentException("successful responses must not contain an error");
        }
        if (!success && data != null) {
            throw new IllegalArgumentException("failed responses must not contain data");
        }
        if (!success && error == null) {
            throw new IllegalArgumentException("failed responses must contain an error");
        }
    }

    public static <T> VaultApiResponse<T> success(RequestContext context, T data) {
        Objects.requireNonNull(context, "context must not be null");
        return new VaultApiResponse<>(true, context.requestId().value(), data, null);
    }

    public static <T> VaultApiResponse<T> failure(
            RequestContext context, VaultErrorCode code, String message) {
        Objects.requireNonNull(context, "context must not be null");
        return new VaultApiResponse<>(
                false, context.requestId().value(), null, new VaultApiError(code, message));
    }
}
