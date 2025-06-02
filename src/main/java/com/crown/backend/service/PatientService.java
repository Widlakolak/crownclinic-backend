package com.crown.backend.service;

import com.crown.backend.domain.Patient;
import com.crown.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient update(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updatedPatient.getFirstName());
                    existing.setLastName(updatedPatient.getLastName());
                    existing.setEmail(updatedPatient.getEmail());
                    existing.setPhone(updatedPatient.getPhone());
                    existing.setDateOfBirth(updatedPatient.getDateOfBirth());
                    return patientRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
    }

    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}
