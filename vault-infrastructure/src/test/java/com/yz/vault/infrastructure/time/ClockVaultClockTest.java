package com.yz.vault.infrastructure.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class ClockVaultClockTest {
    @Test
    void readsTheInjectedFixedClock() {
        var clock = new ClockVaultClock(
                Clock.fixed(Instant.parse("2026-09-22T00:00:00Z"), ZoneOffset.UTC));
        assertEquals(Instant.parse("2026-09-22T00:00:00Z"), clock.now());
    }
}
