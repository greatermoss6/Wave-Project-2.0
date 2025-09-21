package com.signlearn.domain.value;

import com.signlearn.security.PasswordHasher;

import java.util.regex.Pattern;

/**
 * Immutable value object representing a validated password.
 * Internally stores only the HASHED form.
 */
public final class Password {
    private final String hash;

    private static final Pattern UPPER   = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWER   = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT   = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL = Pattern.compile(".*[^a-zA-Z0-9].*");

    /** Construct from RAW password: validates and hashes with the provided hasher. */
    public Password(String raw, PasswordHasher hasher) {
        validate(raw);
        this.hash = hasher.hash(raw);
    }

    /** Factory for constructing from an EXISTING hash (e.g., loaded from DB). */
    public static Password hashed(String existingHash) {
        return new Password(existingHash);
    }

    /** Private ctor for pre-hashed values. No validation on the raw rules; just sanity check. */
    private Password(String existingHash) {
        if (existingHash == null || existingHash.isBlank()) {
            throw new IllegalArgumentException("Existing hash cannot be null or blank.");
        }
        this.hash = existingHash;
    }

    private void validate(String raw) {
        if (raw == null || raw.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        if (!UPPER.matcher(raw).matches())
            throw new IllegalArgumentException("Password must contain an uppercase letter.");
        if (!LOWER.matcher(raw).matches())
            throw new IllegalArgumentException("Password must contain a lowercase letter.");
        if (!DIGIT.matcher(raw).matches())
            throw new IllegalArgumentException("Password must contain a digit.");
        if (!SPECIAL.matcher(raw).matches())
            throw new IllegalArgumentException("Password must contain a special character.");
        if (raw.contains(" "))
            throw new IllegalArgumentException("Password cannot contain spaces.");
    }

    /** Returns the stored hash (never the raw password). */
    public String getHash() {
        return hash;
    }
}