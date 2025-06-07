package com.crown.backend.service;

import com.crown.backend.domain.Appointment;
import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.dto.AppointmentRequestDto;
import com.crown.backend.mapper.AppointmentMapper;
import com.crown.backend.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private GoogleCalendarService googleCalendarService;

    @Mock
    private PatientService patientService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppointmentService appointmentService;

    public AppointmentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAppointment() {
        AppointmentRequestDto dto = new AppointmentRequestDto(
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMinutes(30),
                "Konsultacja",
                1L,
                2L,
                Appointment.Status.SCHEDULED
        );

        Patient mockPatient = Patient.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .build();
        User mockDoctor = User.builder()
                .id(2L)
                .googleCalendarId("calendar123")
                .googleAccessToken("token123")
                .firstName("Anna")
                .lastName("Nowak")
                .build();
        Appointment appointment = Appointment.builder()
                .id(1L)
                .patient(mockPatient)
                .doctor(mockDoctor)
                .notes("Konsultacja")
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .status(Appointment.Status.SCHEDULED)
                .build();

        when(patientService.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(userService.findById(2L)).thenReturn(Optional.of(mockDoctor));

        when(appointmentMapper.toEntity(dto, mockPatient, mockDoctor)).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(googleCalendarService.createEvent(
                anyString(), anyString(), anyString(), anyString(),
                any(ZonedDateTime.class), any(ZonedDateTime.class))
        ).thenReturn("event123");

        appointmentService.createAppointment(dto);

        verify(googleCalendarService, times(1))
                .createEvent(anyString(), anyString(), anyString(), anyString(),
                        any(ZonedDateTime.class), any(ZonedDateTime.class));
    }
}
