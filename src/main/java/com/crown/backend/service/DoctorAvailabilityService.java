package com.crown.backend.service;

import com.crown.backend.domain.DoctorAvailability;
import com.crown.backend.repository.DoctorAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;

    public List<DoctorAvailability> findAll() {
        return availabilityRepository.findAll();
    }

    public Optional<DoctorAvailability> findById(Long id) {
        return availabilityRepository.findById(id);
    }

    public DoctorAvailability save(DoctorAvailability availability) {
        return availabilityRepository.save(availability);
    }

    public void delete(Long id) {
        availabilityRepository.deleteById(id);
    }
}
