package com.yz.vault.contracts.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.yz.vault.contracts.request.RequestContext;
import com.yz.vault.domain.error.VaultErrorCode;
import com.yz.vault.domain.identifier.RequestId;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class VaultApiResponseTest {

    @Test
    void successResponseCarriesRequestIdAndNeverAnErrorPayload() {
        var context = new RequestContext(RequestId.of("req-api-1"), Instant.parse("2026-09-22T00:00:00Z"));

        var response = VaultApiResponse.success(context, "ready");

        assertTrue(response.success());
        assertEquals("req-api-1", response.requestId());
        assertEquals("ready", response.data());
        assertNull(response.error());
    }

    @Test
    void failureResponseDoesNotExposeData() {
        var context = new RequestContext(RequestId.of("req-api-2"), Instant.parse("2026-09-22T00:00:00Z"));

        var response = VaultApiResponse.failure(context, VaultErrorCode.SEALED, "vault is sealed");

        assertFalse(response.success());
        assertNull(response.data());
        assertEquals(VaultErrorCode.SEALED, response.error().code());
        assertEquals("vault is sealed", response.error().message());
    }
}
