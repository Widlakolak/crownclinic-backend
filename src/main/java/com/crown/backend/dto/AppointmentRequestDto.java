package com.crown.backend.dto;

import com.crown.backend.domain.Appointment;

import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record AppointmentRequestDto(
        @NotNull ZonedDateTime startDateTime,
        @NotNull ZonedDateTime endDateTime,
        String notes,
        @NotNull Long patientId,
        @NotNull Long doctorId,
        Appointment.Status status
) {}
