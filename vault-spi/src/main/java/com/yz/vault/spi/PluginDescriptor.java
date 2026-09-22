package com.yz.vault.spi;

import java.util.Objects;
import java.util.Set;

/** Immutable, non-sensitive metadata used to select and diagnose a plugin. */
public record PluginDescriptor(PluginKind kind, String name, String version, Set<PluginCapability> capabilities) {

    public PluginDescriptor {
        Objects.requireNonNull(kind, "kind must not be null");
        name = requireText(name, "name");
        version = requireText(version, "version");
        capabilities = Set.copyOf(Objects.requireNonNull(capabilities, "capabilities must not be null"));
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
