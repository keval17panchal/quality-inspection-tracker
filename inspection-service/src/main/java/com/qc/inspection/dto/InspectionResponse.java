package com.qc.inspection.dto;

import com.qc.inspection.model.DefectType;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspectionResponse {

    private Long id;
    private LocalDate inspectionDate;
    private String machineLineId;
    private DefectType defectType;
    private Severity severity;
    private String remarks;
    private InspectionStatus status;
    private String resolutionNote;
    private LocalDateTime resolvedAt;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InspectionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public InspectionStatus getStatus() { return status; }
    public void setStatus(InspectionStatus status) { this.status = status; }

    public String getResolutionNote() { return resolutionNote; }
    public void setResolutionNote(String resolutionNote) { this.resolutionNote = resolutionNote; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
