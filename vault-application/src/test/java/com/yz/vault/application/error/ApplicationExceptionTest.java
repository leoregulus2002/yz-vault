package com.yz.vault.application.error;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yz.vault.domain.error.VaultErrorCode;
import org.junit.jupiter.api.Test;

class ApplicationExceptionTest {

    @Test
    void retainsApplicationErrorMetadata() {
        var exception = new ApplicationException(
                VaultErrorCode.APPLICATION_OPERATION_FAILED, "save was rejected");

        assertEquals("YZV-200001", exception.code().value());
        assertEquals("save was rejected", exception.getMessage());
    }
}
