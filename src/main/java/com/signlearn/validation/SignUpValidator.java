package com.signlearn.validation;

import com.signlearn.security.PasswordHasher;
import com.signlearn.domain.value.Password;
import com.signlearn.util.ValidationResult;

import java.time.LocalDate;
import java.time.Period;

public class SignUpValidator {
    private final PasswordHasher passwordHasher;

    public SignUpValidator(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public ValidationResult validate(String name,
                                     String username,
                                     String rawPassword,
                                     String confirmPassword,
                                     LocalDate dob) {
        if (name == null || name.isBlank()) {
            return ValidationResult.fail("Name cannot be empty.");
        }

        // Username: 3-20 chars, letters/digits/underscore only
        if (username == null || !username.matches("^[A-Za-z0-9_]{3,20}$")) {
            return ValidationResult.fail("Username must be 3–20 characters (letters, numbers, underscore).");
        }

        if (rawPassword == null || confirmPassword == null) {
            return ValidationResult.fail("Password fields cannot be empty.");
        }
        if (!rawPassword.equals(confirmPassword)) {
            return ValidationResult.fail("Passwords do not match.");
        }

        // Reuse Password validation rules (throws if invalid)
        try {
            new Password(rawPassword, passwordHasher);
        } catch (IllegalArgumentException ex) {
            return ValidationResult.fail(ex.getMessage());
        }

        if (dob == null) {
            return ValidationResult.fail("Please select your date of birth.");
        }
        int years = Period.between(dob, LocalDate.now()).getYears();
        if (years < 0 || years > 99) {
            return ValidationResult.fail("Age must be between 0 and 99.");
        }

        return ValidationResult.success();
    }
}
