package com.crown.backend.controller;

import com.crown.backend.dto.MedicalRecordRequestDto;
import com.crown.backend.dto.MedicalRecordResponseDto;
import com.crown.backend.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> createRecord(
            @RequestPart("record") MedicalRecordRequestDto dto,
            @RequestPart("attachments") MultipartFile[] files) {

        MedicalRecordResponseDto created = medicalRecordService.createRecord(dto, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecordResponseDto> getByPatient(@PathVariable Long patientId) {
        return medicalRecordService.getByPatient(patientId);
    }

    @GetMapping("/{recordId}/attachments/{attachmentId}")
    public ResponseEntity<byte[]> downloadAttachment(
            @PathVariable Long recordId,
            @PathVariable Long attachmentId) {

        return medicalRecordService.getAttachment(recordId, attachmentId)
                .map(attachment -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + attachment.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(attachment.getData()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{recordId}/attachments")
    public ResponseEntity<Void> addAttachments(
            @PathVariable Long recordId,
            @RequestPart("files") MultipartFile[] files) {

        medicalRecordService.addAttachments(recordId, files);
        return ResponseEntity.ok().build();
    }
}

