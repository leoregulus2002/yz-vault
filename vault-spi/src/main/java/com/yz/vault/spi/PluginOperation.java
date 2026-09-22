package com.yz.vault.spi;

/**
 * A type-safe operation submitted to a plugin. Concrete operations, including any sensitive
 * payload representation, are introduced only with their approved capability phase.
 */
public interface PluginOperation<R> {

    PluginKind targetKind();

    PluginCapability requiredCapability();
}
