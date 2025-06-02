package com.crown.backend.controller;

import com.crown.backend.domain.Patient;
import com.crown.backend.dto.PatientRequestDto;
import com.crown.backend.dto.PatientResponseDto;
import com.crown.backend.mapper.PatientMapper;
import com.crown.backend.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllPatients() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Kowalska")
                .email("anna@example.com")
                .build();

        Mockito.when(patientService.findAll()).thenReturn(List.of(patient));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("anna@example.com"));
    }

    @Test
    void shouldReturnPatientById() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Kowalska")
                .email("anna@example.com")
                .build();

        Mockito.when(patientService.findById(1L)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Anna Kowalska"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientRequestDto request = new PatientRequestDto("Anna", "Kowalska", "anna@example.com", "123456789", LocalDate.of(1990, 1, 1));
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Kowalska")
                .email("anna@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        Mockito.when(patientService.save(any())).thenReturn(patient);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Anna Kowalska"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(patientService).delete(1L);
    }
}
