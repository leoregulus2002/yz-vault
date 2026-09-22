package com.yz.vault.spi;

/** Contract for cryptographic provider implementations; no key material crosses this descriptor boundary. */
public interface CryptoProvider {

    PluginDescriptor descriptor();

    <R> R execute(PluginOperation<R> operation);

    default boolean supports(PluginCapability capability) {
        return descriptor().capabilities().contains(capability);
    }
}
