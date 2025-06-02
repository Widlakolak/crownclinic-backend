package com.crown.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MessageThreadDto(
        Long id,
        List<Long> participantIds,
        String subject,
        LocalDateTime lastUpdated
) {}
