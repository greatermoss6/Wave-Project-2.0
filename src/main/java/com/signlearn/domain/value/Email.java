package com.signlearn.domain.value;

import java.util.Objects;
import java.util.regex.Pattern;

/** Immutable email value object: trimmed, lower-cased, validated. */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final int MAX_TOTAL = 254;
    private static final int MAX_LOCAL = 64;

    public Email {
        if (value == null) throw new IllegalArgumentException("Email cannot be null.");

        String normalized = value.trim().toLowerCase();
        if (normalized.isEmpty()) throw new IllegalArgumentException("Email cannot be blank.");
        if (normalized.length() > MAX_TOTAL) throw new IllegalArgumentException("Email too long.");

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }

        int at = normalized.indexOf('@');
        String local = normalized.substring(0, at);
        String domain = normalized.substring(at + 1);

        if (local.isEmpty() || domain.isEmpty())
            throw new IllegalArgumentException("Invalid email: missing local or domain.");
        if (local.length() > MAX_LOCAL)
            throw new IllegalArgumentException("Local part too long.");
        if (domain.startsWith(".") || domain.endsWith(".") || domain.contains(".."))
            throw new IllegalArgumentException("Invalid domain in email.");

        value = normalized;
    }

    /** Convenience factory. */
    public static Email of(String raw) { return new Email(raw); }

    /** Non-throwing validator. */
    public static boolean isValid(String raw) {
        if (raw == null) return false;
        String s = raw.trim().toLowerCase();
        if (s.isEmpty() || s.length() > MAX_TOTAL) return false;
        if (!EMAIL_PATTERN.matcher(s).matches()) return false;
        int at = s.indexOf('@');
        if (at < 1 || at == s.length() - 1) return false;
        String local = s.substring(0, at);
        String domain = s.substring(at + 1);
        if (local.length() > MAX_LOCAL) return false;
        if (domain.startsWith(".") || domain.endsWith(".") || domain.contains("..")) return false;
        return true;
    }

    @Override public String toString() { return value; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override public int hashCode() { return Objects.hash(value); }
}
