package com.signlearn.validation;

import java.util.LinkedHashMap;
import java.util.Map;

public class PasswordStrengthChecker {

    public static class Rules {
        public final int minLength;
        public final boolean requireUpper;
        public final boolean requireLower;
        public final boolean requireDigit;
        public final boolean requireSymbol;

        public Rules(int minLength, boolean requireUpper, boolean requireLower, boolean requireDigit, boolean requireSymbol) {
            this.minLength = minLength;
            this.requireUpper = requireUpper;
            this.requireLower = requireLower;
            this.requireDigit = requireDigit;
            this.requireSymbol = requireSymbol;
        }
    }

    private final Rules rules;

    public PasswordStrengthChecker() {
        this(new Rules(8, true, true, true, true));
    }

    public PasswordStrengthChecker(Rules rules) {
        this.rules = rules;
    }

    public Map<String, Boolean> evaluate(String password) {
        String p = password == null ? "" : password;

        Map<String, Boolean> result = new LinkedHashMap<>();
        result.put("At least " + rules.minLength + " characters", p.length() >= rules.minLength);
        result.put("Contains uppercase letter", !rules.requireUpper || p.chars().anyMatch(Character::isUpperCase));
        result.put("Contains lowercase letter", !rules.requireLower || p.chars().anyMatch(Character::isLowerCase));
        result.put("Contains a digit", !rules.requireDigit || p.chars().anyMatch(Character::isDigit));
        result.put("Contains a symbol", !rules.requireSymbol || p.matches(".*[^A-Za-z0-9].*"));
        return result;
    }

    public boolean allSatisfied(String password) {
        return evaluate(password).values().stream().allMatch(Boolean::booleanValue);
    }
}