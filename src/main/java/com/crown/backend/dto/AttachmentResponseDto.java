package com.crown.backend.dto;

public record AttachmentResponseDto(
        Long id,
        String filename,
        String fileType
) {}