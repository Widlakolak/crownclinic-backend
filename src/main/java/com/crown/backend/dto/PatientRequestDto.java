package com.crown.backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PatientRequestDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth
) {}