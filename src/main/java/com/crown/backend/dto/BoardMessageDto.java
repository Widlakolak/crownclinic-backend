package com.crown.backend.dto;

public record BoardMessageDto(
        Long authorId,
        String content
) {}
