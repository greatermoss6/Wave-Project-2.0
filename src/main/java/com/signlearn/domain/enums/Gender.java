package com.signlearn.domain.enums;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String display;

    Gender(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        return display; // for UI controls
    }

    /**
     * Map DB string (enum name or display text) to Gender.
     */
    public static Gender fromDbValue(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) return PREFER_NOT_TO_SAY;

        // match enum name
        for (Gender g : values()) {
            if (g.name().equalsIgnoreCase(dbValue)) return g;
        }
        // match display label
        for (Gender g : values()) {
            if (g.display.equalsIgnoreCase(dbValue)) return g;
        }
        return PREFER_NOT_TO_SAY;
    }

    /**
     * Value to store in DB.
     */
    public String toDbValue() {
        // either return name() or display; keeping display for readability
        return display;
    }

    /**
     * Maps a human-readable string back into a Gender enum.
     */
    public static Gender fromDisplayName(String displayName) {
        for (Gender g : values()) {
            if (g.display.equalsIgnoreCase(displayName)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Unknown gender: " + displayName);
    }

}