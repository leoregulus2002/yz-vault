package com.yz.vault.infrastructure.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yz.vault.domain.identifier.RequestId;
import org.junit.jupiter.api.Test;

class SupplierIdentifierGeneratorTest {
    @Test
    void returnsValuesFromTheInjectedSupplier() {
        var generator = new SupplierIdentifierGenerator<>(() -> RequestId.of("req-1"));
        assertEquals(RequestId.of("req-1"), generator.next());
    }
}
