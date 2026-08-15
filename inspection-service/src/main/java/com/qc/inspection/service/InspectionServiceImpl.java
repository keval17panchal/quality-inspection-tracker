package com.qc.inspection.service;

import com.qc.inspection.dto.*;
import com.qc.inspection.entity.Inspection;
import com.qc.inspection.exception.InvalidOperationException;
import com.qc.inspection.exception.ResourceNotFoundException;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import com.qc.inspection.repository.InspectionRepository;
import com.qc.inspection.repository.InspectionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InspectionServiceImpl implements InspectionService {

    private final InspectionRepository inspectionRepository;

    public InspectionServiceImpl(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    public InspectionResponse createInspection(CreateInspectionRequest request) {
        Inspection inspection = new Inspection();
        inspection.setInspectionDate(request.getInspectionDate());
        inspection.setMachineLineId(request.getMachineLineId().trim());
        inspection.setDefectType(request.getDefectType());
        inspection.setSeverity(request.getSeverity());
        inspection.setRemarks(request.getRemarks());
        inspection.setStatus(InspectionStatus.OPEN);
        inspection.setSource("MANUAL");

        Inspection saved = inspectionRepository.save(inspection);
        return mapToResponse(saved);
    }

    @Override
    public InspectionResponse createFromSapWebhook(SapWebhookRequest request) {
        Inspection inspection = new Inspection();
        inspection.setInspectionDate(request.getInspectionDate());
        inspection.setMachineLineId(request.getMachineLineId().trim());
        inspection.setDefectType(request.getDefectType());
        inspection.setSeverity(request.getSeverity());
        inspection.setRemarks(request.getRemarks());
        inspection.setStatus(InspectionStatus.OPEN);
        inspection.setSource(request.getSource() != null ? request.getSource() : "SAP");

        Inspection saved = inspectionRepository.save(inspection);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InspectionResponse getInspectionById(Long id) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id: " + id));
        return mapToResponse(inspection);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InspectionResponse> getInspections(
            Severity severity,
            InspectionStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            String machineLineId,
            Pageable pageable
    ) {
        var spec = InspectionSpecification.filter(severity, status, fromDate, toDate, machineLineId);
        Page<Inspection> page = inspectionRepository.findAll(spec, pageable);

        List<InspectionResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                responses,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public InspectionResponse resolveInspection(Long id, ResolveInspectionRequest request) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id: " + id));

        if (inspection.getStatus() == InspectionStatus.RESOLVED) {
            throw new InvalidOperationException("Inspection with id " + id + " is already resolved");
        }

        inspection.setStatus(InspectionStatus.RESOLVED);
        inspection.setResolutionNote(request.getResolutionNote().trim());
        inspection.setResolvedAt(LocalDateTime.now());

        Inspection updated = inspectionRepository.save(inspection);
        return mapToResponse(updated);
    }

    @Override
    public InspectionResponse updateInspection(Long id, UpdateInspectionRequest request) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id: " + id));

        inspection.setInspectionDate(request.getInspectionDate());
        inspection.setMachineLineId(request.getMachineLineId().trim());
        inspection.setDefectType(request.getDefectType());
        inspection.setSeverity(request.getSeverity());
        inspection.setRemarks(request.getRemarks());

        Inspection updated = inspectionRepository.save(inspection);
        return mapToResponse(updated);
    }

    @Override
    public void deleteInspection(Long id) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id: " + id));
        inspectionRepository.delete(inspection);
    }

    @Override
    @Transactional(readOnly = true)
    public InspectionSummaryResponse getSummary() {
        List<Object[]> rawCounts = inspectionRepository.countByStatusAndSeverityGrouped();

        long openCritical = 0, openMajor = 0, openMinor = 0;
        long resolvedCritical = 0, resolvedMajor = 0, resolvedMinor = 0;

        for (Object[] row : rawCounts) {
            InspectionStatus status = (InspectionStatus) row[0];
            Severity severity = (Severity) row[1];
            long count = ((Number) row[2]).longValue();

            if (status == InspectionStatus.OPEN) {
                if (severity == Severity.CRITICAL) openCritical = count;
                else if (severity == Severity.MAJOR) openMajor = count;
                else if (severity == Severity.MINOR) openMinor = count;
            } else if (status == InspectionStatus.RESOLVED) {
                if (severity == Severity.CRITICAL) resolvedCritical = count;
                else if (severity == Severity.MAJOR) resolvedMajor = count;
                else if (severity == Severity.MINOR) resolvedMinor = count;
            }
        }

        InspectionSummaryResponse.StatusSummary openSummary =
                new InspectionSummaryResponse.StatusSummary(openCritical, openMajor, openMinor);
        InspectionSummaryResponse.StatusSummary resolvedSummary =
                new InspectionSummaryResponse.StatusSummary(resolvedCritical, resolvedMajor, resolvedMinor);

        return new InspectionSummaryResponse(openSummary, resolvedSummary);
    }

    private InspectionResponse mapToResponse(Inspection inspection) {
        InspectionResponse response = new InspectionResponse();
        response.setId(inspection.getId());
        response.setInspectionDate(inspection.getInspectionDate());
        response.setMachineLineId(inspection.getMachineLineId());
        response.setDefectType(inspection.getDefectType());
        response.setSeverity(inspection.getSeverity());
        response.setRemarks(inspection.getRemarks());
        response.setStatus(inspection.getStatus());
        response.setResolutionNote(inspection.getResolutionNote());
        response.setResolvedAt(inspection.getResolvedAt());
        response.setSource(inspection.getSource());
        response.setCreatedAt(inspection.getCreatedAt());
        response.setUpdatedAt(inspection.getUpdatedAt());
        return response;
    }
}
