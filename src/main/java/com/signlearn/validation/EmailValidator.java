package com.signlearn.validation;

import com.signlearn.util.ValidationResult;

public class EmailValidator {

    private static final java.util.regex.Pattern EMAIL =
            java.util.regex.Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public ValidationResult validate(String email) {
        if (email == null || email.isBlank()) {
            return ValidationResult.fail("Email cannot be empty.");
        }
        if (!EMAIL.matcher(email).matches()) {
            return ValidationResult.fail("Invalid email format. Example: user@example.com");
        }
        return ValidationResult.success();
    }
}
