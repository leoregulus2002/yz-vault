package com.yz.vault.infrastructure.random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class JavaSecureRandomSourceTest {
    @Test
    void fillsTheProvidedBufferAndRejectsNull() {
        var source = new JavaSecureRandomSource(new DeterministicSecureRandom());
        var bytes = new byte[32];

        source.nextBytes(bytes);

        assertThrows(NullPointerException.class, () -> source.nextBytes(null));
        assertArrayEquals(new byte[] {0x5a, 0x5a, 0x5a, 0x5a}, Arrays.copyOf(bytes, 4));
    }

    private static final class DeterministicSecureRandom extends SecureRandom {
        @Override
        public void nextBytes(byte[] bytes) {
            Arrays.fill(bytes, (byte) 0x5a);
        }
    }
}
