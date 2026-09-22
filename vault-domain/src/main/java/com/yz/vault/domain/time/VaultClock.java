package com.yz.vault.domain.time;

import java.time.Instant;

/** Provides the current time without coupling domain behavior to the system clock. */
@FunctionalInterface
public interface VaultClock {

    Instant now();
}
