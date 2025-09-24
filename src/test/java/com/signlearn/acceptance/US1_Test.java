package com.signlearn.acceptance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class US1_Test
    {
        private boolean resourceExistsCheck(String filePath)
        {
            return US1_Test.class.getResource(filePath) != null;
        }

        @Test
        void logoExistsCheck()
        {
            boolean exists = resourceExistsCheck("/images/logo.png");
            assertTrue(exists, "Logo image exists in project path /images/logo.png");
        }
        // Testing if unit test suite works
    }
