package com.signlearn.validation;

import com.signlearn.domain.service.UserService;
import com.signlearn.util.ValidationResult;

import java.util.Collections;
import java.util.List;

/**
 * Encapsulates all logic around validating and suggesting usernames.
 * Keeps SignUpDeepController thin by handling business + presentation state logic.
 */
public class UsernameValidator {

    private final UserService userService;
    private static final String PATTERN = "^[A-Za-z0-9_]{3,20}$";

    public UsernameValidator(UserService userService) {
        this.userService = userService;
    }

    /** Validate only format. */
    public ValidationResult validateFormat(String username) {
        if (username == null || !username.matches(PATTERN)) {
            return ValidationResult.fail("Username must be 3–20 characters (letters, numbers, underscore).");
        }
        return ValidationResult.success();
    }

    /** Validate uniqueness in DB. */
    public ValidationResult validateUniqueness(String username) {
        if (userService.existsByUsername(username)) {
            return ValidationResult.fail("That username is already taken.");
        }
        return ValidationResult.success();
    }

    /**
     * Validate format + uniqueness and provide suggestions if taken.
     * Backward compatible with your old design.
     */
    public UsernameValidationResult validateWithSuggestions(String username, int suggestionCount) {
        ValidationResult format = validateFormat(username);
        if (!format.isSuccess()) {
            return new UsernameValidationResult(format, List.of());
        }

        boolean taken = userService.existsByUsername(username);
        if (taken) {
            List<String> suggestions = userService.suggestUsernames(username, suggestionCount);
            return new UsernameValidationResult(
                    ValidationResult.fail("That username is already taken."),
                    suggestions
            );
        }
        return new UsernameValidationResult(ValidationResult.success(), List.of());
    }

    // --- New OOP additions for UI state management ---

    /**
     * Called when user leaves the username field.
     * Ensures suggestions should not remain visible anymore.
     */
    public UsernameValidationResult handleFocusLost(String username) {
        ValidationResult result = validateFormat(username);
        if (!result.isSuccess()) {
            return new UsernameValidationResult(result, Collections.emptyList());
        }

        // Keep red/green border but hide suggestions
        if (userService.existsByUsername(username)) {
            return new UsernameValidationResult(
                    ValidationResult.fail("That username is already taken."),
                    Collections.emptyList()
            );
        }
        return new UsernameValidationResult(ValidationResult.success(), Collections.emptyList());
    }

    /**
     * Called when user selects a suggestion.
     * That suggestion is guaranteed unique → mark as success, hide others.
     */
    public UsernameValidationResult handleSuggestionChosen(String chosenUsername) {
        return new UsernameValidationResult(ValidationResult.success(), Collections.emptyList());
    }

    // --- Nested Result type ---
    public static class UsernameValidationResult {
        private final ValidationResult result;
        private final List<String> suggestions;

        public UsernameValidationResult(ValidationResult result, List<String> suggestions) {
            this.result = result;
            this.suggestions = suggestions;
        }

        public ValidationResult getResult() { return result; }
        public List<String> getSuggestions() { return suggestions; }

        public boolean hasSuggestions() {
            return suggestions != null && !suggestions.isEmpty();
        }
    }
}
