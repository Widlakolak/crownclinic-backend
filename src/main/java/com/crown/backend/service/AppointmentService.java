package com.crown.backend.service;

import com.crown.backend.domain.Appointment;
import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.dto.AppointmentRequestDto;
import com.crown.backend.dto.AppointmentResponseDto;
import com.crown.backend.mapper.AppointmentMapper;
import com.crown.backend.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarService googleCalendarService;
    private final PatientService patientService;
    private final UserService userService;
    private final AppointmentMapper appointmentMapper;

    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<AppointmentResponseDto> getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toDto);
    }

    @Transactional
    public AppointmentResponseDto createAppointment(AppointmentRequestDto dto) {
        Patient patient = patientService.findById(dto.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pacjenta z ID: " + dto.patientId()));

        User doctor = userService.findById(dto.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono lekarza z ID: " + dto.doctorId()));

        Appointment appointment = appointmentMapper.toEntity(dto, patient, doctor);
        Appointment saved = appointmentRepository.save(appointment);

        // Utwórz wydarzenie w kalendarzu Google
        if (doctor.getGoogleCalendarId() != null) {
            String eventId = googleCalendarService.createEvent(
                    doctor.getGoogleCalendarId(),
                    "Wizyta: " + patient.getFirstName() + " " + patient.getLastName(),
                    "Gabinet: Crown Clinic\nLekarz: " + doctor.getFirstName() + " " + doctor.getLastName(),
                    appointment.getStartDateTime(),
                    appointment.getEndDateTime()
            );
            saved.setGoogleEventId(eventId);
            appointmentRepository.save(saved);
        }

        return appointmentMapper.toDto(saved);
    }

    @Transactional
    public AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto dto) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wizyta nie znaleziona"));

        Patient patient = patientService.findById(dto.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pacjenta"));

        User doctor = userService.findById(dto.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono lekarza"));

        existing.setStartDateTime(dto.startDateTime());
        existing.setEndDateTime(dto.endDateTime());
        existing.setPatient(patient);
        existing.setDoctor(doctor);
        existing.setStatus(dto.status());
        existing.setNotes(dto.notes());

        if (existing.getGoogleEventId() != null && doctor.getGoogleCalendarId() != null) {
            googleCalendarService.updateEvent(
                    doctor.getGoogleCalendarId(),
                    existing.getGoogleEventId(),
                    "ZAKTUALIZOWANA wizyta: " + patient.getFirstName() + " " + patient.getLastName(),
                    "Gabinet: Crown Clinic\nLekarz: " + doctor.getFirstName() + " " + doctor.getLastName(),
                    existing.getStartDateTime(),
                    existing.getEndDateTime()
            );
        }

        return appointmentMapper.toDto(appointmentRepository.save(existing));
    }

    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wizyta nie znaleziona"));

        if (appointment.getGoogleEventId() != null && appointment.getDoctor().getGoogleCalendarId() != null) {
            googleCalendarService.deleteEvent(
                    appointment.getDoctor().getGoogleCalendarId(),
                    appointment.getGoogleEventId()
            );
        }

        appointmentRepository.deleteById(id);
    }
}
