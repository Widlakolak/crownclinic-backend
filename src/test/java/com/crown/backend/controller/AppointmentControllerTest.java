package com.crown.backend.controller;

import com.crown.backend.domain.Appointment;
import com.crown.backend.dto.AppointmentRequestDto;
import com.crown.backend.dto.AppointmentResponseDto;
import com.crown.backend.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllAppointments() throws Exception {
        AppointmentResponseDto dto = new AppointmentResponseDto(
                1L, ZonedDateTime.now(), ZonedDateTime.now().plusMinutes(30),
                "Uwagi", Appointment.Status.SCHEDULED,
                "Dr Smith", "smith@example.com",
                "Anna Kowalska", "anna@example.com",
                null
        );

        Mockito.when(appointmentService.getAllAppointments()).thenReturn(List.of(dto));

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorFullName").value("Dr Smith"));
    }

    @Test
    void shouldCreateAppointment() throws Exception {
        AppointmentRequestDto request = new AppointmentRequestDto(
                ZonedDateTime.now(), ZonedDateTime.now().plusMinutes(30),
                "Konsultacja", 1L, 2L, Appointment.Status.SCHEDULED
        );

        AppointmentResponseDto response = new AppointmentResponseDto(
                1L, request.startDateTime(), request.endDateTime(),
                request.notes(), request.status(),
                "Dr Smith", "smith@example.com",
                "Anna Kowalska", "anna@example.com",
                null
        );

        Mockito.when(appointmentService.createAppointment(any())).thenReturn(response);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
