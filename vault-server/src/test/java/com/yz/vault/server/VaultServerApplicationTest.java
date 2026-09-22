package com.yz.vault.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = VaultServerApplication.class)
class VaultServerApplicationTest {

    @Test
    void startsAnApplicationContextWithoutSecretConfiguration() {
        // A Phase 1 server must start without embedding any secret, key, or database credential.
    }
}
