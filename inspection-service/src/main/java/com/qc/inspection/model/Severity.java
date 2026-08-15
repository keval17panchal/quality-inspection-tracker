package com.qc.inspection.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Severity {
    CRITICAL("Critical"),
    MAJOR("Major"),
    MINOR("Minor");

    private final String displayName;

    Severity(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Severity fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (Severity s : Severity.values()) {
            if (s.displayName.equalsIgnoreCase(text.trim()) || s.name().equalsIgnoreCase(text.trim())) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown Severity: " + text);
    }
}
