package com.qc.inspection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class UpdateInspectionRequest {

    @NotNull(message = "Inspection date is required")
    private LocalDate inspectionDate;

    @NotBlank(message = "Machine / Line ID is required")
    private String machineLineId;

    @NotNull(message = "Defect type is required")
    private com.qc.inspection.model.DefectType defectType;

    @NotNull(message = "Severity is required")
    private com.qc.inspection.model.Severity severity;

    private String remarks;

    public UpdateInspectionRequest() {}

    public UpdateInspectionRequest(LocalDate inspectionDate, String machineLineId, com.qc.inspection.model.DefectType defectType, com.qc.inspection.model.Severity severity, String remarks) {
        this.inspectionDate = inspectionDate;
        this.machineLineId = machineLineId;
        this.defectType = defectType;
        this.severity = severity;
        this.remarks = remarks;
    }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public String getMachineLineId() { return machineLineId; }
    public void setMachineLineId(String machineLineId) { this.machineLineId = machineLineId; }

    public com.qc.inspection.model.DefectType getDefectType() { return defectType; }
    public void setDefectType(com.qc.inspection.model.DefectType defectType) { this.defectType = defectType; }

    public com.qc.inspection.model.Severity getSeverity() { return severity; }
    public void setSeverity(com.qc.inspection.model.Severity severity) { this.severity = severity; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
