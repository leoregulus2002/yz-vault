package com.yz.vault.domain.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class VaultIdentifiersTest {

    @Test
    void rejectsBlankRequestIdToPreventAmbiguousAuditCorrelation() {
        assertThrows(IllegalArgumentException.class, () -> RequestId.of("  "));
    }

    @Test
    void preservesValidatedRequestIdValueForCrossBoundaryCorrelation() {
        assertEquals("req-9cf8", RequestId.of("req-9cf8").value());
    }

    @Test
    void rejectsBlankKeyVersionIdToProtectKeyVersionAddressing() {
        assertThrows(IllegalArgumentException.class, () -> KeyVersionId.of(""));
    }
}
