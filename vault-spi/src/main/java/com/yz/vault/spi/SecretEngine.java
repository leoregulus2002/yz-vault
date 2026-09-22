package com.yz.vault.spi;

/** Contract for secret-engine implementations; concrete read/write operations arrive in later phases. */
public interface SecretEngine {

    PluginDescriptor descriptor();

    <R> R execute(PluginOperation<R> operation);

    default boolean supports(PluginCapability capability) {
        return descriptor().capabilities().contains(capability);
    }
}
