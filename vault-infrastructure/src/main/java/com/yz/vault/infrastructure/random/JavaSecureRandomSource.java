package com.yz.vault.infrastructure.random;

import com.yz.vault.domain.random.SecureRandomSource;
import java.security.SecureRandom;
import java.util.Objects;

/** Adapts {@link SecureRandom} to the domain random-byte port. */
public final class JavaSecureRandomSource implements SecureRandomSource {

    private final SecureRandom secureRandom;

    public JavaSecureRandomSource(SecureRandom secureRandom) {
        this.secureRandom = Objects.requireNonNull(secureRandom, "secureRandom must not be null");
    }

    @Override
    public void nextBytes(byte[] destination) {
        secureRandom.nextBytes(Objects.requireNonNull(destination, "destination must not be null"));
    }
}
