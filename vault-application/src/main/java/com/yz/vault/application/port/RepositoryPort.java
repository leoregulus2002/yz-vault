package com.yz.vault.application.port;

import java.util.Optional;

/** Persistence-neutral repository contract for future application use cases. */
public interface RepositoryPort<E, ID> {

    Optional<E> findById(ID id);

    E save(E entity);

    boolean deleteById(ID id);
}
