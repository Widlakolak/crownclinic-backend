package com.crown.backend.controller;

import com.crown.backend.domain.Patient;
import com.crown.backend.dto.PatientRequestDto;
import com.crown.backend.dto.PatientResponseDto;
import com.crown.backend.mapper.PatientMapper;
import com.crown.backend.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    public List<PatientResponseDto> getAllPatients() {
        return patientService.findAll()
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        return patientService.findById(id)
                .map(patientMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody @Valid PatientRequestDto dto) {
        Patient saved = patientService.save(patientMapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/api/patients/" + saved.getId()))
                .body(patientMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long id,
            @RequestBody @Valid PatientRequestDto dto) {
        try {
            Patient updated = patientService.update(id, patientMapper.toEntity(dto));
            return ResponseEntity.ok(patientMapper.toDto(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
