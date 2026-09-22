package com.yz.vault.domain.error;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DomainExceptionTest {

    @Test
    void retainsStableCodeAndSafeMessage() {
        var exception = new DomainException(
                VaultErrorCode.DOMAIN_INVARIANT_VIOLATION, "lease id is invalid");

        assertEquals("YZV-100001", exception.code().value());
        assertEquals("lease id is invalid", exception.getMessage());
    }
}
