package com.crown.backend.mapper;

import com.crown.backend.domain.MedicalRecord;
import com.crown.backend.dto.MedicalRecordResponseDto;
import com.crown.backend.mapper.AttachmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MedicalRecordMapper {

    private final AttachmentMapper attachmentMapper;

    public MedicalRecordResponseDto toDto(MedicalRecord record) {
        return new MedicalRecordResponseDto(
                record.getId(),
                record.getTitle(),
                record.getDescription(),
                record.getCreatedAt(),
                record.getCreatedBy().getFirstName() + " " + record.getCreatedBy().getLastName(),
                record.getPatient().getFirstName() + " " + record.getPatient().getLastName(),
                record.getAttachments().stream()
                        .map(attachmentMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}
