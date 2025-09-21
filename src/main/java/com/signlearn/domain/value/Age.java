package com.signlearn.domain.value;

/**
 * Value object representing a valid age.
 */
public final class Age {
    private final int value;

    private Age(int value) {
        this.value = value;
    }

    public static Age of(int value) {
        if (value < 0 || value > 120) {
            throw new IllegalArgumentException("Invalid age: " + value);
        }
        return new Age(value);
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}