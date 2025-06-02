package com.crown.backend.dto;

import java.time.LocalDate;

public record PatientResponseDto(
        Long id,
        String fullName,
        String email,
        String phone,
        LocalDate dateOfBirth
) {}
