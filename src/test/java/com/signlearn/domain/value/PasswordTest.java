package com.signlearn.domain.value;

import com.signlearn.security.PasswordHasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    /** Simple deterministic hasher stub for testing (no real hashing). */
    private static class StubHasher implements PasswordHasher {
        @Override public String hash(String raw) {
            return "HASH(" + raw + ")";
        }
        @Override public boolean matches(String raw, String hashed) {
            return ("HASH(" + raw + ")").equals(hashed);
        }
    }

    @Test
    @DisplayName("Valid password is accepted and stored as a hash (not raw)")
    void validPasswordCreatesHashedValue() {
        String raw = "Str0ng@Pass"; // upper + lower + digit + special, no spaces, >= 8 chars
        Password p = new Password(raw, new StubHasher());

        assertNotNull(p.getHash(), "Hash should be generated");
        assertFalse(p.getHash().isBlank(), "Hash should not be blank");
        assertNotEquals(raw, p.getHash(), "Hash must not equal the raw password");
        assertEquals("HASH(Str0ng@Pass)", p.getHash());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short!",            // < 8 chars / missing classes
            "alllowercase1!",    // no uppercase
            "ALLUPPERCASE1!",    // no lowercase
            "NoDigits!!",        // no digit
            "NoSpecial123",      // no special char
            "With space1!",      // contains space
            "            ",       // blanks
            ""                    // empty
    })
    @DisplayName("Invalid passwords are rejected")
    void invalidPasswordsThrow(String raw) {
        assertThrows(IllegalArgumentException.class, () -> new Password(raw, new StubHasher()));
    }

    @Test
    @DisplayName("Different valid inputs produce different hashes with the stub")
    void differentInputsProduceDifferentHashes() {
        Password p1 = new Password("Str0ng@Pass1", new StubHasher());
        Password p2 = new Password("Str0ng@Pass2", new StubHasher());

        assertEquals("HASH(Str0ng@Pass1)", p1.getHash());
        assertEquals("HASH(Str0ng@Pass2)", p2.getHash());
        assertNotEquals(p1.getHash(), p2.getHash());
    }

    @Test
    @DisplayName("Factory 'hashed' wraps an existing hash without validation of raw rules")
    void hashedFactoryUsesExistingHash() {
        Password p = Password.hashed("already-hashed-value");
        assertEquals("already-hashed-value", p.getHash());
    }
}
