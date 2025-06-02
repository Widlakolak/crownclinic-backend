package com.crown.backend.mapper;

import com.crown.backend.domain.Attachment;
import com.crown.backend.domain.MedicalRecord;
import com.crown.backend.dto.MedicalRecordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalRecordMapper {

    public MedicalRecordResponseDto toDto(MedicalRecord record) {
        return new MedicalRecordResponseDto(
                record.getId(),
                record.getTitle(),
                record.getDescription(),
                record.getCreatedAt(),
                record.getCreatedBy().getFirstName() + " " + record.getCreatedBy().getLastName(),
                record.getPatient().getFirstName() + " " + record.getPatient().getLastName(),
                record.getAttachments().stream().map(Attachment::getFilename).toList()
        );
    }
}
