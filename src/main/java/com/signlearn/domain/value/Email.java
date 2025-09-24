package com.signlearn.domain.value;

import java.util.Objects;

/**
 * Value object representing an Email.
 * Immutable and validated.
 */
public class Email {
    private final String address;

    public Email(String address) {
        if (address == null || !address.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email: " + address);
        }
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    /** Alias for backwards-compatibility if some code calls getValue() */
    public String getValue() {
        return address;
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return address.equalsIgnoreCase(email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address.toLowerCase());
    }
}
