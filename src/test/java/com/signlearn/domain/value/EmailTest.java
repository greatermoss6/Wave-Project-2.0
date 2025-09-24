package com.signlearn.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    // ---------- Valid cases ----------

    @ParameterizedTest
    @ValueSource(strings = {
            "user@example.com",
            "USER@EXAMPLE.COM",
            " user.name+tag-1@example.co.uk ",
            "u_s-e.r%test@sub.domain.io"
    })
    @DisplayName("Valid emails construct and are normalized (trim + lowercase)")
    void validEmailsNormalize(String raw) {
        Email e = new Email(raw);
        assertEquals(raw.trim().toLowerCase(), e.value());
        assertTrue(Email.isValid(raw));
    }

    @Test
    @DisplayName("Equality is case-insensitive due to normalization")
    void equalityAfterNormalization() {
        Email a = new Email("User@Example.com");
        Email b = new Email("user@example.com");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("user@example.com", a.toString());
    }

    // ---------- Invalid basic cases ----------

    @Test
    @DisplayName("Null and blank are rejected")
    void nullAndBlankRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
        assertThrows(IllegalArgumentException.class, () -> new Email("   "));
        assertFalse(Email.isValid(null));
        assertFalse(Email.isValid("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "no-at-symbol.com",
            "user@domain",              // no TLD
            "user@.domain.com",         // domain starts with dot
            "user@domain..com",         // double dot in domain
            "user@domain.",             // domain ends with dot
            "user name@domain.com"      // space in local
    })
    @DisplayName("Clearly invalid formats are rejected")
    void invalidFormats(String raw) {
        assertThrows(IllegalArgumentException.class, () -> new Email(raw));
        assertFalse(Email.isValid(raw));
    }

    // ---------- Length constraints ----------

    @Test
    @DisplayName("Local part > 64 chars is rejected")
    void localPartTooLong() {
        String local = "a".repeat(65);
        String raw = local + "@example.com";
        assertThrows(IllegalArgumentException.class, () -> new Email(raw));
        assertFalse(Email.isValid(raw));
    }

    @Test
    @DisplayName("Total length > 254 chars is rejected")
    void totalLengthTooLong() {
        // "@example.com" is 12 chars; make total = 255
        String local = "a".repeat(255 - "@example.com".length());
        String raw = local + "@example.com"; // length 255
        assertThrows(IllegalArgumentException.class, () -> new Email(raw));
        assertFalse(Email.isValid(raw));
    }

    // ---------- isValid consistency check ----------

    @ParameterizedTest
    @ValueSource(strings = {"ok@example.com", "OK@EXAMPLE.COM", "x@y.co"})
    @DisplayName("isValid matches constructor success for good inputs")
    void isValidMatchesConstructorOnGoodInputs(String raw) {
        assertTrue(Email.isValid(raw));
        assertDoesNotThrow(() -> new Email(raw));
    }
}
