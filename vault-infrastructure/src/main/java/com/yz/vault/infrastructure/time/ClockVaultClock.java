package com.yz.vault.infrastructure.time;

import com.yz.vault.domain.time.VaultClock;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/** Adapts an injected Java clock to the domain clock port. */
public final class ClockVaultClock implements VaultClock {

    private final Clock clock;

    public ClockVaultClock(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public Instant now() {
        return clock.instant();
    }
}
