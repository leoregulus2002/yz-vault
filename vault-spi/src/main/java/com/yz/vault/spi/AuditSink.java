package com.yz.vault.spi;

/** Receives secret-free audit events from the application layer. */
public interface AuditSink {

    PluginDescriptor descriptor();

    void record(AuditEvent event);

    default boolean supports(PluginCapability capability) {
        return descriptor().capabilities().contains(capability);
    }
}
