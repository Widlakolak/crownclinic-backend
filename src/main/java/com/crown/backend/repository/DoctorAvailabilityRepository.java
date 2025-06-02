package com.crown.backend.repository;

import com.crown.backend.domain.DoctorAvailability;
import com.crown.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findAllByDoctor(User doctor);
}
