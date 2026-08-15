package com.qc.inspection.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InspectionStatus {
    OPEN("Open"),
    RESOLVED("Resolved");

    private final String displayName;

    InspectionStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static InspectionStatus fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (InspectionStatus status : InspectionStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text.trim()) || status.name().equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown InspectionStatus: " + text);
    }
}
