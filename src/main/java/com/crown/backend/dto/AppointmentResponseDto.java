package com.crown.backend.dto;

import com.crown.backend.domain.Appointment;

import java.time.ZonedDateTime;

public record AppointmentResponseDto(
        Long id,
        ZonedDateTime startDateTime,
        ZonedDateTime endDateTime,
        String notes,
        Appointment.Status status,
        String doctorFullName,
        String doctorEmail,
        String patientFullName,
        String patientEmail,
        String googleEventId
) {}
