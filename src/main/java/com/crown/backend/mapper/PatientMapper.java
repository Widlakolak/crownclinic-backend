package com.crown.backend.mapper;

import com.crown.backend.domain.Patient;
import com.crown.backend.dto.PatientRequestDto;
import com.crown.backend.dto.PatientResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequestDto dto) {
        return Patient.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phone(dto.phone())
                .dateOfBirth(dto.dateOfBirth())
                .build();
    }

    public PatientResponseDto toDto(Patient patient) {
        return new PatientResponseDto(
                patient.getId(),
                patient.getFirstName() + " " + patient.getLastName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getDateOfBirth()
        );
    }
}
