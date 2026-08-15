package com.qc.inspection.controller;

import com.qc.inspection.dto.*;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import com.qc.inspection.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@Tag(name = "Inspection Management", description = "APIs for managing quality inspections")
@CrossOrigin(origins = "*")
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PostMapping("/inspections")
    @Operation(summary = "Create a new quality inspection")
    public ResponseEntity<InspectionResponse> createInspection(@Valid @RequestBody CreateInspectionRequest request) {
        InspectionResponse response = inspectionService.createInspection(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/inspections")
    @Operation(summary = "Get paginated, sorted, and filtered list of quality inspections")
    public ResponseEntity<PageResponse<InspectionResponse>> getInspections(
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) InspectionStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String machineLineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<InspectionResponse> response = inspectionService.getInspections(
                severity, status, fromDate, toDate, machineLineId, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inspections/{id}")
    @Operation(summary = "Get inspection details by ID")
    public ResponseEntity<InspectionResponse> getInspectionById(@PathVariable Long id) {
        InspectionResponse response = inspectionService.getInspectionById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/inspections/{id}/resolve")
    @Operation(summary = "Resolve an open quality inspection")
    public ResponseEntity<InspectionResponse> resolveInspection(
            @PathVariable Long id,
            @Valid @RequestBody ResolveInspectionRequest request
    ) {
        InspectionResponse response = inspectionService.resolveInspection(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/inspections/{id}")
    @Operation(summary = "Edit an existing quality inspection")
    public ResponseEntity<InspectionResponse> updateInspection(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInspectionRequest request
    ) {
        InspectionResponse response = inspectionService.updateInspection(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/inspections/{id}")
    @Operation(summary = "Delete an inspection")
    public ResponseEntity<Void> deleteInspection(@PathVariable Long id) {
        inspectionService.deleteInspection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get summary counts grouped by Status and Severity")
    public ResponseEntity<InspectionSummaryResponse> getSummary() {
        InspectionSummaryResponse response = inspectionService.getSummary();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sap-webhook")
    @Operation(summary = "Mock SAP Webhook Endpoint to record inspections from external system")
    public ResponseEntity<InspectionResponse> handleSapWebhook(@Valid @RequestBody SapWebhookRequest request) {
        InspectionResponse response = inspectionService.createFromSapWebhook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
