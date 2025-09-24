package com.signlearn.security;

public interface PasswordHasher {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashed);

    // Backwards-compat alias
    default boolean verify(String rawPassword, String hashed) {
        return matches(rawPassword, hashed);
    }
}