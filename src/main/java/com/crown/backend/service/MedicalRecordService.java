package com.crown.backend.service;

import com.crown.backend.domain.Attachment;
import com.crown.backend.domain.MedicalRecord;
import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.dto.MedicalRecordRequestDto;
import com.crown.backend.dto.MedicalRecordResponseDto;
import com.crown.backend.mapper.MedicalRecordMapper;
import com.crown.backend.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientService patientService;
    private final UserService userService;
    private final MedicalRecordMapper mapper;

    public MedicalRecordResponseDto createRecord(
            MedicalRecordRequestDto dto,
            MultipartFile[] files
    ) {
        Patient patient = patientService.findById(dto.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        User doctor = userService.findById(dto.createdById())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        MedicalRecord record = MedicalRecord.builder()
                .title(dto.title())
                .description(dto.description())
                .patient(patient)
                .createdBy(doctor)
                .createdAt(LocalDateTime.now())
                .archived(false)
                .build();

        for (MultipartFile file : files) {
            try {
                record.getAttachments().add(Attachment.builder()
                        .filename(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .data(file.getBytes())
                        .medicalRecord(record)
                        .build());
            } catch (IOException e) {
                throw new RuntimeException("File error: " + file.getOriginalFilename());
            }
        }

        return mapper.toDto(medicalRecordRepository.save(record));
    }

    public List<MedicalRecordResponseDto> getByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId)
                .stream().map(mapper::toDto)
                .toList();
    }

    public Optional<Attachment> getAttachment(Long recordId, Long attachmentId) {
        return medicalRecordRepository.findById(recordId)
                .flatMap(record -> record.getAttachments()
                        .stream()
                        .filter(a -> a.getId().equals(attachmentId))
                        .findFirst());
    }

    public void addAttachments(Long recordId, MultipartFile[] files) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("MedicalRecord not found"));

        for (MultipartFile file : files) {
            try {
                record.getAttachments().add(Attachment.builder()
                        .filename(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .data(file.getBytes())
                        .medicalRecord(record)
                        .build());
            } catch (IOException e) {
                throw new RuntimeException("File error: " + file.getOriginalFilename());
            }
        }

        medicalRecordRepository.save(record);
    }
}
