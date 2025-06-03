package com.crown.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record AttachmentRequestDto(
        @NotBlank String filename,
        @NotBlank String fileType
) {}