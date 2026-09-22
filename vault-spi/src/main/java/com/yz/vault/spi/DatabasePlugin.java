package com.yz.vault.spi;

/** Contract for dynamic database credential providers. */
public interface DatabasePlugin {

    PluginDescriptor descriptor();

    <R> R execute(PluginOperation<R> operation);

    default boolean supports(PluginCapability capability) {
        return descriptor().capabilities().contains(capability);
    }
}
