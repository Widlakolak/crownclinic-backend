package com.crown.backend.service;

import com.crown.backend.domain.Patient;
import com.crown.backend.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void shouldSavePatient() {
        Patient patient = Patient.builder()
                .firstName("Anna")
                .lastName("Nowak")
                .email("anna@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        when(patientRepository.save(any())).thenReturn(patient);

        Patient saved = patientService.save(patient);

        assertEquals("Anna", saved.getFirstName());
        assertEquals("Nowak", saved.getLastName());
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldReturnAllPatients() {
        List<Patient> list = List.of(new Patient(), new Patient());
        when(patientRepository.findAll()).thenReturn(list);

        List<Patient> result = patientService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnPatientById() {
        Patient patient = new Patient();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> found = patientService.findById(1L);

        assertTrue(found.isPresent());
    }

    @Test
    void shouldUpdatePatient() {
        Patient existing = Patient.builder().id(1L).firstName("Jan").lastName("Kowalski").build();
        Patient updated = Patient.builder().firstName("Adam").lastName("Nowak").build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any())).thenReturn(existing);

        Patient result = patientService.update(1L, updated);

        assertEquals("Adam", result.getFirstName());
    }

    @Test
    void shouldDeletePatient() {
        patientService.delete(5L);
        verify(patientRepository).deleteById(5L);
    }
}