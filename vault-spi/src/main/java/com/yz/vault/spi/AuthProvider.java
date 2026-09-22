package com.yz.vault.spi;

/** Contract for authentication-method implementations. */
public interface AuthProvider {

    PluginDescriptor descriptor();

    <R> R execute(PluginOperation<R> operation);

    default boolean supports(PluginCapability capability) {
        return descriptor().capabilities().contains(capability);
    }
}
