package com.crown.backend.mapper;

import com.crown.backend.domain.Appointment;
import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.dto.AppointmentRequestDto;
import com.crown.backend.dto.AppointmentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentRequestDto dto, Patient patient, User doctor) {
        return Appointment.builder()
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .notes(dto.notes())
                .status(dto.status() != null ? dto.status() : Appointment.Status.SCHEDULED)
                .patient(patient)
                .doctor(doctor)
                .build();
    }

    public AppointmentResponseDto toDto(Appointment appointment) {
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getStartDateTime(),
                appointment.getEndDateTime(),
                appointment.getNotes(),
                appointment.getStatus(),
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                appointment.getDoctor().getEmail(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                appointment.getPatient().getEmail(),
                appointment.getGoogleEventId()
        );
    }
}
