package com.yz.vault.spi;

/** Contract for durable storage implementations selected by the application layer. */
public interface Storage {

    PluginDescriptor descriptor();

    <R> R execute(PluginOperation<R> operation);

    default boolean supports(PluginCapability capability) {
        return descriptor().capabilities().contains(capability);
    }
}
