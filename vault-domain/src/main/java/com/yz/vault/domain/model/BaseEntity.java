package com.yz.vault.domain.model;

import java.util.Objects;

/** Base class for domain entities with immutable identity and type-safe equality. */
public abstract class BaseEntity<ID> {

    private final ID id;

    protected BaseEntity(ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public final ID id() {
        return id;
    }

    @Override
    public final boolean equals(Object other) {
        return this == other
                || (other != null
                        && getClass() == other.getClass()
                        && id.equals(((BaseEntity<?>) other).id));
    }

    @Override
    public final int hashCode() {
        return 31 * getClass().hashCode() + id.hashCode();
    }
}
