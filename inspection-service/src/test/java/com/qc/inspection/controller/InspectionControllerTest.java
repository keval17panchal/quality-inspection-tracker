package com.qc.inspection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qc.inspection.dto.CreateInspectionRequest;
import com.qc.inspection.dto.ResolveInspectionRequest;
import com.qc.inspection.dto.SapWebhookRequest;
import com.qc.inspection.model.DefectType;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import com.qc.inspection.service.InspectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qc.inspection.security.JwtTokenProvider;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;

@WebMvcTest(InspectionController.class)
@AutoConfigureMockMvc(addFilters = false)
class InspectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InspectionService inspectionService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private com.qc.inspection.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void testCreateInspection_ValidationFailure() throws Exception {
        CreateInspectionRequest invalidRequest = new CreateInspectionRequest();
        // Missing required fields

        mockMvc.perform(post("/api/inspections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.machineLineId").exists())
                .andExpect(jsonPath("$.errors.inspectionDate").exists());
    }

    @Test
    void testResolveInspection_ValidationFailure_BlankNote() throws Exception {
        ResolveInspectionRequest invalidRequest = new ResolveInspectionRequest("");

        mockMvc.perform(patch("/api/inspections/1/resolve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.resolutionNote").value("Resolution note is mandatory"));
    }
}
