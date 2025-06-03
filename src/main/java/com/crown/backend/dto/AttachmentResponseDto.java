package com.crown.backend.dto;

import com.crown.backend.domain.Attachment;

public record AttachmentResponseDto(
        Long id,
        String filename,
        String fileType,
        Attachment.AttachmentType attachmentType
) {}
