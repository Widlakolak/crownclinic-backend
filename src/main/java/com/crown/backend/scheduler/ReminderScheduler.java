package com.crown.backend.scheduler;

import com.crown.backend.domain.Appointment;
import com.crown.backend.service.EmailService;
import com.crown.backend.domain.User;
import com.crown.backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *") // codziennie o 09:00
    public void remindPatients() {
        LocalDateTime start = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<Appointment> appointments = appointmentRepository.findAllByStartDateTimeBetweenAndStatus(
                start, end, Appointment.Status.SCHEDULED);

        for (Appointment a : appointments) {
            String patientEmail = a.getPatient().getEmail();
            String subject = "Przypomnienie o wizycie";
            String body = "Dzień dobry " + a.getPatient().getFirstName() + ",\n\n"
                    + "Przypominamy o Twojej wizycie jutro o godzinie: "
                    + a.getStartDateTime().toLocalTime() + "\n"
                    + "Lekarz: " + a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName() + "\n\n"
                    + "Pozdrawiamy,\nZespół Crown Clinic";

            emailService.send(patientEmail, subject, body);
        }
    }

    @Scheduled(cron = "0 0 17 * * *") // codziennie o 17:00
    public void notifyDoctors() {
        LocalDateTime start = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = start.plusDays(1);

        List<Appointment> appointments = appointmentRepository.findAllByStartDateTimeBetweenAndStatus(
                start, end, Appointment.Status.SCHEDULED);

        Map<User, List<Appointment>> grouped = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDoctor));

        for (Map.Entry<User, List<Appointment>> entry : grouped.entrySet()) {
            User doctor = entry.getKey();
            List<Appointment> list = entry.getValue();

            String email = doctor.getEmail();
            String subject = "Twój jutrzejszy grafik";
            String firstTime = list.stream()
                    .map(Appointment::getStartDateTime)
                    .min(Comparator.naturalOrder())
                    .orElse(start.atZone(ZoneId.systemDefault()))
                    .toLocalTime()
                    .toString();

            String lastTime = list.stream()
                    .map(Appointment::getEndDateTime)
                    .max(Comparator.naturalOrder())
                    .orElse(end.atZone(ZoneId.systemDefault()))
                    .toLocalTime()
                    .toString();

            String body = "Dzień dobry Dr. " + doctor.getLastName() + ",\n\n"
                    + "Masz zaplanowane " + list.size() + " wizyt jutro.\n"
                    + "Pracujesz od " + firstTime + " do " + lastTime + ".\n\n"
                    + "Pozdrawiamy,\nZespół Crown Clinic";

            emailService.send(email, subject, body);
        }
    }
}