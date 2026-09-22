package com.yz.vault.infrastructure.identifier;

import com.yz.vault.domain.identifier.IdentifierGenerator;
import java.util.Objects;
import java.util.function.Supplier;

/** Adapts an injected supplier to a typed domain identifier generator. */
public final class SupplierIdentifierGenerator<T> implements IdentifierGenerator<T> {

    private final Supplier<T> supplier;

    public SupplierIdentifierGenerator(Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier, "supplier must not be null");
    }

    @Override
    public T next() {
        return Objects.requireNonNull(supplier.get(), "generated identifier must not be null");
    }
}
