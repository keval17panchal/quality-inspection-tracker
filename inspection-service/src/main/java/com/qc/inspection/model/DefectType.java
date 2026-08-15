package com.qc.inspection.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DefectType {
    WEAVE_DEFECT("Weave Defect"),
    SHADE_VARIATION("Shade Variation"),
    HOLE_TEAR("Hole/Tear"),
    COUNT_DEVIATION("Count Deviation"),
    OTHER("Other");

    private final String displayName;

    DefectType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static DefectType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (DefectType b : DefectType.values()) {
            if (b.displayName.equalsIgnoreCase(text.trim()) || b.name().equalsIgnoreCase(text.trim())) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown DefectType: " + text);
    }
}
