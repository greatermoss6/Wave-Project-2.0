package com.signlearn.domain.value;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void validEmailTest()
    {
        Email testEmail = new Email("myemail@gmail.com");
        assertEquals("myemail@gmail.com", testEmail.value());
    }

    @Test
    void nullEmailTest()
    {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }
}
