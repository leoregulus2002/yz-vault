package com.yz.vault.application;

import com.yz.vault.contracts.api.VaultApiResponse;
import com.yz.vault.contracts.request.RequestContext;

/** Typed application boundary implemented by future Vault use cases. */
@FunctionalInterface
public interface VaultOperation<I, O> {

    VaultApiResponse<O> execute(RequestContext context, I input);
}
