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
}
