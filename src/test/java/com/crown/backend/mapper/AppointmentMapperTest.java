package com.crown.backend.mapper;

import com.crown.backend.domain.Appointment;
import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.dto.AppointmentRequestDto;
import com.crown.backend.dto.AppointmentResponseDto;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentMapperTest {

    private final AppointmentMapper mapper = new AppointmentMapper();

    @Test
    void shouldMapToEntity() {
        ZonedDateTime now = ZonedDateTime.now();
        AppointmentRequestDto dto = new AppointmentRequestDto(now, now.plusMinutes(30), "Uwagi", 1L, 2L, Appointment.Status.SCHEDULED);

        Patient patient = Patient.builder().id(1L).firstName("Anna").lastName("Nowak").email("anna@example.com").build();
        User doctor = User.builder().id(2L).firstName("Dr").lastName("Kowalski").email("dr@example.com").build();

        Appointment result = mapper.toEntity(dto, patient, doctor);

        assertEquals("Uwagi", result.getNotes());
        assertEquals(patient, result.getPatient());
        assertEquals(doctor, result.getDoctor());
    }

    @Test
    void shouldMapToDto() {
        ZonedDateTime now = ZonedDateTime.now();
        Appointment appointment = Appointment.builder()
                .id(1L)
                .startDateTime(now)
                .endDateTime(now.plusMinutes(30))
                .notes("Wizyta")
                .status(Appointment.Status.SCHEDULED)
                .patient(Patient.builder().firstName("Anna").lastName("Nowak").email("anna@example.com").build())
                .doctor(User.builder().firstName("Dr").lastName("Kowalski").email("dr@example.com").build())
                .build();

        AppointmentResponseDto dto = mapper.toDto(appointment);

        assertEquals("Dr Kowalski", dto.doctorFullName());
        assertEquals("Anna Nowak", dto.patientFullName());
    }
}
