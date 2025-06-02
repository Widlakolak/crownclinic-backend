package com.crown.backend.dto;

import com.crown.backend.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        String phone,
        String googleCalendarId,
        User.Role role
) {}
