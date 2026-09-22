package com.yz.vault.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Bootstrap entry point for the independently deployable yz-vault server. */
@SpringBootApplication
public class VaultServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultServerApplication.class, args);
    }
}
