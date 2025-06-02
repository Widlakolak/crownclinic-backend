package com.crown.backend.scheduler;

import com.crown.backend.domain.*;
import com.crown.backend.repository.AppointmentRepository;
import com.crown.backend.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReminderSchedulerTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReminderScheduler reminderScheduler;

    @Test
    void shouldSendPatientReminderEmails() {
        // given
        Patient patient = Patient.builder().email("anna@example.com").firstName("Anna").build();
        User doctor = User.builder().firstName("Dr").lastName("Smith").build();

        Appointment appointment = Appointment.builder()
                .startDateTime(ZonedDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0))
                .status(Appointment.Status.SCHEDULED)
                .patient(patient)
                .doctor(doctor)
                .build();


        when(appointmentRepository.findAllByStartDateTimeBetweenAndStatus(any(), any(), eq(Appointment.Status.SCHEDULED)))
                .thenReturn(List.of(appointment));

        // when
        reminderScheduler.remindPatients();

        // then
        verify(emailService).send(
                eq("anna@example.com"),
                contains("Przypomnienie"),
                contains("Dr Smith")
        );
    }

    @Test
    void shouldSendDoctorSummaryEmails() {
        // Stała data z dokładną godziną i strefą czasową
        ZonedDateTime start = ZonedDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0), ZoneId.systemDefault());
        ZonedDateTime end = start.plusHours(1);
        ZonedDateTime secondStart = start.plusHours(2);
        ZonedDateTime secondEnd = start.plusHours(3);

        User doctor = User.builder()
                .email("smith@example.com")
                .firstName("Dr")
                .lastName("Smith")
                .build();

        Appointment a1 = Appointment.builder()
                .startDateTime(start)
                .endDateTime(end)
                .doctor(doctor)
                .status(Appointment.Status.SCHEDULED)
                .build();

        Appointment a2 = Appointment.builder()
                .startDateTime(secondStart)
                .endDateTime(secondEnd)
                .doctor(doctor)
                .status(Appointment.Status.SCHEDULED)
                .build();

        when(appointmentRepository.findAllByStartDateTimeBetweenAndStatus(any(), any(), eq(Appointment.Status.SCHEDULED)))
                .thenReturn(List.of(a1, a2));

        // when
        reminderScheduler.notifyDoctors();

        // then
        verify(emailService).send(
                eq("smith@example.com"),
                contains("grafik"),
                contains("od 09:00")
        );
    }

}
