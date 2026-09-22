package com.yz.vault.spi;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yz.vault.domain.identifier.RequestId;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class AuditEventTest {

    @Test
    void rejectsBlankActionToKeepAuditEventsQueryable() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AuditEvent(
                        RequestId.of("req-audit-1"), " ", true, Instant.parse("2026-09-22T00:00:00Z")));
    }
}
