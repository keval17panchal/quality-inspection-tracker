package com.qc.inspection.dto;

import com.qc.inspection.model.DefectType;
import com.qc.inspection.model.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateInspectionRequest {

    @NotNull(message = "Inspection date is required")
    private LocalDate inspectionDate;

    @NotBlank(message = "Machine / Line ID is required")
    private String machineLineId;

    @NotNull(message = "Defect type is required")
    private DefectType defectType;

    @NotNull(message = "Severity is required")
    private Severity severity;

    private String remarks;

    public CreateInspectionRequest() {}

    public CreateInspectionRequest(LocalDate inspectionDate, String machineLineId, DefectType defectType, Severity severity, String remarks) {
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

    public DefectType getDefectType() { return defectType; }
    public void setDefectType(DefectType defectType) { this.defectType = defectType; }

    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
