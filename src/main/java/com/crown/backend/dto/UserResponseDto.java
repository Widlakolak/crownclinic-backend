package com.crown.backend.dto;

import com.crown.backend.domain.User;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String googleCalendarId,
        User.Role role
) {}