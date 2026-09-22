package com.yz.vault.application.port;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RepositoryPortTest {

    @Test
    void repositoryPortSupportsAnExplicitMissingResult() {
        RepositoryPort<String, String> repository = new InMemoryRepository();

        assertTrue(repository.findById("missing").isEmpty());
        assertEquals("value", repository.save("value"));
        assertTrue(repository.deleteById("value"));
    }

    private static final class InMemoryRepository implements RepositoryPort<String, String> {

        private final Map<String, String> values = new HashMap<>();

        @Override
        public Optional<String> findById(String id) {
            return Optional.ofNullable(values.get(id));
        }

        @Override
        public String save(String entity) {
            values.put(entity, entity);
            return entity;
        }

        @Override
        public boolean deleteById(String id) {
            return values.remove(id) != null;
        }
    }
}
