package com.crown.backend.repository;

import com.crown.backend.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDoctorId(Long doctorId);
    List<Appointment> findAllByStartDateTimeBetweenAndStatus(
            LocalDateTime start, LocalDateTime end, Appointment.Status status);
}

