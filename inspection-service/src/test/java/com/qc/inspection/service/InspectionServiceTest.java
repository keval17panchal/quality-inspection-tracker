package com.qc.inspection.service;

import com.qc.inspection.dto.*;
import com.qc.inspection.entity.Inspection;
import com.qc.inspection.exception.InvalidOperationException;
import com.qc.inspection.exception.ResourceNotFoundException;
import com.qc.inspection.model.DefectType;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import com.qc.inspection.repository.InspectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspectionServiceTest {

    @Mock
    private InspectionRepository inspectionRepository;

    @InjectMocks
    private InspectionServiceImpl inspectionService;

    private Inspection sampleInspection;

    @BeforeEach
    void setUp() {
        sampleInspection = new Inspection();
        sampleInspection.setId(1L);
        sampleInspection.setInspectionDate(LocalDate.now());
        sampleInspection.setMachineLineId("LINE-01");
        sampleInspection.setDefectType(DefectType.WEAVE_DEFECT);
        sampleInspection.setSeverity(Severity.CRITICAL);
        sampleInspection.setRemarks("Loose threads");
        sampleInspection.setStatus(InspectionStatus.OPEN);
        sampleInspection.setSource("MANUAL");
    }

    @Test
    void testCreateInspection_Success() {
        CreateInspectionRequest request = new CreateInspectionRequest(
                LocalDate.now(), "LINE-01", DefectType.WEAVE_DEFECT, Severity.CRITICAL, "Loose threads"
        );

        when(inspectionRepository.save(any(Inspection.class))).thenReturn(sampleInspection);

        InspectionResponse response = inspectionService.createInspection(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("LINE-01", response.getMachineLineId());
        assertEquals(InspectionStatus.OPEN, response.getStatus());
        verify(inspectionRepository, times(1)).save(any(Inspection.class));
    }

    @Test
    void testCreateFromSapWebhook_Success() {
        SapWebhookRequest request = new SapWebhookRequest();
        request.setInspectionDate(LocalDate.now());
        request.setMachineLineId("SAP-LINE-02");
        request.setDefectType(DefectType.SHADE_VARIATION);
        request.setSeverity(Severity.MAJOR);
        request.setRemarks("Shade mismatch from SAP");
        request.setSource("SAP_ERP");

        Inspection sapInspection = new Inspection();
        sapInspection.setId(2L);
        sapInspection.setInspectionDate(request.getInspectionDate());
        sapInspection.setMachineLineId("SAP-LINE-02");
        sapInspection.setDefectType(DefectType.SHADE_VARIATION);
        sapInspection.setSeverity(Severity.MAJOR);
        sapInspection.setRemarks("Shade mismatch from SAP");
        sapInspection.setStatus(InspectionStatus.OPEN);
        sapInspection.setSource("SAP_ERP");

        when(inspectionRepository.save(any(Inspection.class))).thenReturn(sapInspection);

        InspectionResponse response = inspectionService.createFromSapWebhook(request);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("SAP-LINE-02", response.getMachineLineId());
        assertEquals("SAP_ERP", response.getSource());
        assertEquals(InspectionStatus.OPEN, response.getStatus());
    }

    @Test
    void testGetInspectionById_Success() {
        when(inspectionRepository.findById(1L)).thenReturn(Optional.of(sampleInspection));

        InspectionResponse response = inspectionService.getInspectionById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetInspectionById_NotFound() {
        when(inspectionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inspectionService.getInspectionById(99L));
    }

    @Test
    void testGetInspections_FilteringAndPagination() {
        Page<Inspection> page = new PageImpl<>(List.of(sampleInspection));
        when(inspectionRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        PageResponse<InspectionResponse> response = inspectionService.getInspections(
                Severity.CRITICAL, InspectionStatus.OPEN, null, null, "LINE", PageRequest.of(0, 10)
        );

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testResolveInspection_Success() {
        ResolveInspectionRequest request = new ResolveInspectionRequest("Machine calibrated and thread tension fixed.");

        when(inspectionRepository.findById(1L)).thenReturn(Optional.of(sampleInspection));
        when(inspectionRepository.save(any(Inspection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InspectionResponse response = inspectionService.resolveInspection(1L, request);

        assertNotNull(response);
        assertEquals(InspectionStatus.RESOLVED, response.getStatus());
        assertEquals("Machine calibrated and thread tension fixed.", response.getResolutionNote());
        assertNotNull(response.getResolvedAt());
    }

    @Test
    void testResolveInspection_AlreadyResolved() {
        sampleInspection.setStatus(InspectionStatus.RESOLVED);
        sampleInspection.setResolutionNote("Already fixed");

        when(inspectionRepository.findById(1L)).thenReturn(Optional.of(sampleInspection));

        ResolveInspectionRequest request = new ResolveInspectionRequest("Trying to resolve again");

        assertThrows(InvalidOperationException.class, () -> inspectionService.resolveInspection(1L, request));
    }

    @Test
    void testGetSummary_Success() {
        List<Object[]> mockCounts = new ArrayList<>();
        mockCounts.add(new Object[]{InspectionStatus.OPEN, Severity.CRITICAL, 3L});
        mockCounts.add(new Object[]{InspectionStatus.OPEN, Severity.MAJOR, 8L});
        mockCounts.add(new Object[]{InspectionStatus.OPEN, Severity.MINOR, 12L});

        mockCounts.add(new Object[]{InspectionStatus.RESOLVED, Severity.CRITICAL, 5L});
        mockCounts.add(new Object[]{InspectionStatus.RESOLVED, Severity.MAJOR, 14L});
        mockCounts.add(new Object[]{InspectionStatus.RESOLVED, Severity.MINOR, 18L});

        when(inspectionRepository.countByStatusAndSeverityGrouped()).thenReturn(mockCounts);

        InspectionSummaryResponse summary = inspectionService.getSummary();

        assertNotNull(summary);
        assertEquals(3, summary.getOpen().getCritical());
        assertEquals(8, summary.getOpen().getMajor());
        assertEquals(12, summary.getOpen().getMinor());
        assertEquals(23, summary.getOpen().getTotal());

        assertEquals(5, summary.getResolved().getCritical());
        assertEquals(14, summary.getResolved().getMajor());
        assertEquals(18, summary.getResolved().getMinor());
        assertEquals(37, summary.getResolved().getTotal());
    }
}
