package com.yz.vault.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class BaseEntityTest {

    @Test
    void comparesOnlyEntitiesOfTheSameConcreteTypeAndIdentifier() {
        assertEquals(new Alpha("id-1"), new Alpha("id-1"));
        assertNotEquals(new Alpha("id-1"), new Alpha("id-2"));
        assertNotEquals(new Alpha("id-1"), new Beta("id-1"));
    }

    @Test
    void exposesItsImmutableIdentifier() {
        assertEquals("id-1", new Alpha("id-1").id());
    }

    private static final class Alpha extends BaseEntity<String> {

        private Alpha(String id) {
            super(id);
        }
    }

    private static final class Beta extends BaseEntity<String> {

        private Beta(String id) {
            super(id);
        }
    }
}
