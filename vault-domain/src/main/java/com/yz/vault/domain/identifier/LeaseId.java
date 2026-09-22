package com.yz.vault.domain.identifier;

/** Identifies one issued lease independently of the secret material it protects. */
public record LeaseId(String value) {

    public LeaseId {
        value = RequestId.requireValue(value, "leaseId");
    }

    public static LeaseId of(String value) {
        return new LeaseId(value);
    }
}
