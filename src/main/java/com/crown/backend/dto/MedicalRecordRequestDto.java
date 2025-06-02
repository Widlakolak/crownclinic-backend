package com.crown.backend.dto;

public record MedicalRecordRequestDto(
        String title,
        String description,
        Long patientId,
        Long createdById
) {}