package com.yz.vault.infrastructure.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.yz.vault.domain.error.VaultErrorCode;
import org.junit.jupiter.api.Test;

class InfrastructureExceptionTest {
    @Test
    void retainsStableCodeSafeMessageAndCause() {
        var cause = new IllegalStateException("adapter unavailable");
        var exception = new InfrastructureException(
                VaultErrorCode.INFRASTRUCTURE_FAILURE, "storage failed", cause);

        assertEquals("YZV-300001", exception.code().value());
        assertEquals("storage failed", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
