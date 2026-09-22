package com.yz.vault.domain.random;

/** Supplies cryptographically secure random bytes through an infrastructure-provided implementation. */
@FunctionalInterface
public interface SecureRandomSource {

    void nextBytes(byte[] destination);
}
