package com.qc.inspection.service;

import com.qc.inspection.dto.*;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface InspectionService {

    InspectionResponse createInspection(CreateInspectionRequest request);

    InspectionResponse createFromSapWebhook(SapWebhookRequest request);

    InspectionResponse getInspectionById(Long id);

    PageResponse<InspectionResponse> getInspections(
            Severity severity,
            InspectionStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            String machineLineId,
            Pageable pageable
    );

    InspectionResponse resolveInspection(Long id, ResolveInspectionRequest request);

    InspectionResponse updateInspection(Long id, UpdateInspectionRequest request);

    void deleteInspection(Long id);

    InspectionSummaryResponse getSummary();
}
