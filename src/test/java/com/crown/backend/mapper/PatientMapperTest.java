package com.crown.backend.mapper;

import com.crown.backend.domain.Patient;
import com.crown.backend.dto.PatientRequestDto;
import com.crown.backend.dto.PatientResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private final PatientMapper mapper = new PatientMapper();

    @Test
    void shouldMapDtoToEntity() {
        PatientRequestDto dto = new PatientRequestDto("Anna", "Nowak", "anna@example.com", "123456789", LocalDate.of(1990, 1, 1));
        Patient entity = mapper.toEntity(dto);
        assertEquals("Anna", entity.getFirstName());
        assertEquals("Nowak", entity.getLastName());
    }

    @Test
    void shouldMapEntityToDto() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Nowak")
                .email("anna@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        PatientResponseDto dto = mapper.toDto(patient);
        assertEquals("Anna Nowak", dto.fullName());
        assertEquals("anna@example.com", dto.email());
    }
}
