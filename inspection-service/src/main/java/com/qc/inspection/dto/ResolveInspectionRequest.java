package com.qc.inspection.dto;

import jakarta.validation.constraints.NotBlank;

public class ResolveInspectionRequest {

    @NotBlank(message = "Resolution note is mandatory")
    private String resolutionNote;

    public ResolveInspectionRequest() {}

    public ResolveInspectionRequest(String resolutionNote) {
        this.resolutionNote = resolutionNote;
    }

    public String getResolutionNote() { return resolutionNote; }
    public void setResolutionNote(String resolutionNote) { this.resolutionNote = resolutionNote; }
}
