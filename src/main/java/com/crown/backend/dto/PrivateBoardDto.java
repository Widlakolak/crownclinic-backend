package com.crown.backend.dto;

import java.util.List;

public record PrivateBoardDto(
        String title,
        List<Long> participantIds
) {}
