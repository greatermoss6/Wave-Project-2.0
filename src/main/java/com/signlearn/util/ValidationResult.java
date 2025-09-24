package com.signlearn.util;

/**
 * Represents the result of validating user input.
 */
public class ValidationResult {
    private final boolean valid;
    private final String message; // success or error message

    private ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    // --- Factory methods ---
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }

    public static ValidationResult fail(String message) {
        return new ValidationResult(false, message);
    }

    // --- Queries ---
    public boolean isValid() {
        return valid;
    }

    public boolean isSuccess() {
        return valid;
    }

    public boolean isFailure() {
        return !valid;
    }

    // --- Messages ---
    public String getMessage() {
        return message;
    }

    public String getMessageOrDefault(String defaultMsg) {
        return message != null ? message : defaultMsg;
    }

    public String getError() {
        return !valid ? message : null;
    }

    public String getErrorMessage() {
        return getError();
    }
}
