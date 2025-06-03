package com.crown.backend.mapper;

import com.crown.backend.domain.Attachment;
import com.crown.backend.dto.AttachmentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {

    public AttachmentResponseDto toDto(Attachment attachment) {
        return new AttachmentResponseDto(
                attachment.getId(),
                attachment.getFilename(),
                attachment.getContentType(),
                attachment.getAttachmentType()
        );
    }
}
