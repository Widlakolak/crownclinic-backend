package com.crown.backend.controller;

import com.crown.backend.config.JwtFilter;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { JwtFilter.class })
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientMapper patientMapper;

    @Test
    void shouldReturnPatientById() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Nowak")
                .email("anna@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        PatientResponseDto dto = new PatientResponseDto(
                1L, "Anna Nowak", "anna@example.com", "123456789", LocalDate.of(1990, 1, 1)
        );

        Mockito.when(patientService.findById(1L)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(dto);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("anna@example.com"))
                .andExpect(jsonPath("$.fullName").value("Anna Nowak"));
    }

    @Test
    void shouldReturnAllPatients() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Nowak")
                .email("anna@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        PatientResponseDto dto = new PatientResponseDto(
                1L, "Anna Nowak", "anna@example.com", "123456789", LocalDate.of(1990, 1, 1)
        );

        Mockito.when(patientService.findAll()).thenReturn(List.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(dto);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("anna@example.com"))
                .andExpect(jsonPath("$[0].fullName").value("Anna Nowak"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientRequestDto request = new PatientRequestDto(
                "Anna", "Nowak", "anna@example.com", "123456789", LocalDate.of(1990, 1, 1)
        );

        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Nowak")
                .email("anna@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        PatientResponseDto dto = new PatientResponseDto(
                1L, "Anna Nowak", "anna@example.com", "123456789", LocalDate.of(1990, 1, 1)
        );

        Mockito.when(patientMapper.toEntity(request)).thenReturn(patient);
        Mockito.when(patientService.save(patient)).thenReturn(patient);
        Mockito.when(patientMapper.toDto(patient)).thenReturn(dto);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Anna Nowak"))
                .andExpect(jsonPath("$.email").value("anna@example.com"));
    }
}