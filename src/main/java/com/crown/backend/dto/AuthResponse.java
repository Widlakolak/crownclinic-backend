package com.crown.backend.dto;

public record AuthResponse(
        String token,
        Long userId,
        String email,
        String role
) {}