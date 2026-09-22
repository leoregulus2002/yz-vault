package com.yz.vault.spi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class PluginDescriptorTest {

    @Test
    void copiesCapabilitiesSoPluginMetadataCannotChangeAfterRegistration() {
        var capabilities = EnumSet.of(PluginCapability.READ_SECRET);
        var descriptor = new PluginDescriptor(
                PluginKind.SECRET_ENGINE, "kv", "v1", capabilities);

        capabilities.add(PluginCapability.WRITE_SECRET);

        assertTrue(descriptor.capabilities().contains(PluginCapability.READ_SECRET));
        assertFalse(descriptor.capabilities().contains(PluginCapability.WRITE_SECRET));
        assertThrows(
                UnsupportedOperationException.class,
                () -> descriptor.capabilities().add(PluginCapability.WRITE_SECRET));
    }
}
