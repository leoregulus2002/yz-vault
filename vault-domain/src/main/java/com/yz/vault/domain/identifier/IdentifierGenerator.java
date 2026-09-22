package com.yz.vault.domain.identifier;

/** Produces typed identifiers without selecting an identifier implementation in the domain. */
@FunctionalInterface
public interface IdentifierGenerator<T> {

    T next();
}
