package com.signlearn.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates password strength rules.
 * Each rule has a description and a satisfaction flag.
 */
public class PasswordValidator {

    /**
     * Represents one password rule result.
     */
    public static class Rule {
        private final String description;
        private final boolean satisfied;

        public Rule(String description, boolean satisfied) {
            this.description = description;
            this.satisfied = satisfied;
        }

        public String getDescription() {
            return description;
        }

        public boolean isSatisfied() {
            return satisfied;
        }
    }

    /**
     * Evaluate a password against all defined rules.
     * @param password raw password string
     * @return list of rule results
     */
    public static List<Rule> evaluate(String password) {
        List<Rule> rules = new ArrayList<>();

        if (password == null) password = "";

        rules.add(new Rule("At least 8 characters", password.length() >= 8));
        rules.add(new Rule("Contains uppercase letter", password.matches(".*[A-Z].*")));
        rules.add(new Rule("Contains lowercase letter", password.matches(".*[a-z].*")));
        rules.add(new Rule("Contains a digit", password.matches(".*\\d.*")));
        rules.add(new Rule("Contains a special character (!@#$%^&*)", password.matches(".*[!@#$%^&*].*")));

        return rules;
    }

    /**
     * Check overall strength (all rules satisfied).
     */
    public static boolean isStrong(String password) {
        return evaluate(password).stream().allMatch(Rule::isSatisfied);
    }
}
