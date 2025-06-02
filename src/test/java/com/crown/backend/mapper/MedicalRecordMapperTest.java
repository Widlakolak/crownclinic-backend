package com.crown.backend.mapper;

import com.crown.backend.domain.Attachment;
import com.crown.backend.domain.MedicalRecord;
import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.dto.MedicalRecordResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordMapperTest {

    private final MedicalRecordMapper mapper = new MedicalRecordMapper();

    @Test
    void shouldMapMedicalRecordToDto() {
        // given
        Patient patient = Patient.builder()
                .firstName("Anna")
                .lastName("Kowalska")
                .build();

        User doctor = User.builder()
                .firstName("Piotr")
                .lastName("Wie")
                .build();

        Attachment attachment1 = Attachment.builder()
                .filename("rtg1.png")
                .build();

        Attachment attachment2 = Attachment.builder()
                .filename("rtg2.png")
                .build();

        MedicalRecord record = MedicalRecord.builder()
                .id(1L)
                .title("Wizyta kontrolna")
                .description("Pacjent w dobrym stanie")
                .createdAt(LocalDateTime.of(2024, 6, 1, 10, 0))
                .createdBy(doctor)
                .patient(patient)
                .attachments(List.of(attachment1, attachment2))
                .build();

        // when
        MedicalRecordResponseDto dto = mapper.toDto(record);

        // then
        assertEquals(1L, dto.id());
        assertEquals("Wizyta kontrolna", dto.title());
        assertEquals("Pacjent w dobrym stanie", dto.description());
        assertEquals("Piotr Wie", dto.createdBy());
        assertEquals("Anna Kowalska", dto.patientName());
        assertEquals(2, dto.attachmentNames().size());
        assertTrue(dto.attachmentNames().contains("rtg1.png"));
        assertTrue(dto.attachmentNames().contains("rtg2.png"));
    }
}
