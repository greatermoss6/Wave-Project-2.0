package com.signlearn.domain.value;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgeTest {
    @Test void validBoundaryValues() {
        assertDoesNotThrow(() -> Age.of(0));
        assertDoesNotThrow(() -> Age.of(120));
    }
    @Test void invalidTooLow() { assertThrows(IllegalArgumentException.class, () -> Age.of(-1)); }
    @Test void invalidTooHigh() { assertThrows(IllegalArgumentException.class, () -> Age.of(121)); }

    @Test void invalidAgeMessage() {
        IllegalArgumentException low = assertThrows(IllegalArgumentException.class, () -> Age.of(-1));
        IllegalArgumentException high = assertThrows(IllegalArgumentException.class, () -> Age.of(121));
        String msgBelowBound = low.getMessage();
        String msgAboveBound = high.getMessage();

        assertTrue(msgBelowBound.contains("age between 0 and 120 are allowed, inclusive"));
        assertTrue(msgAboveBound.contains("age between 0 and 120 are allowed, inclusive"));
    }
}
