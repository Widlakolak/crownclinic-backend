package com.crown.backend.dto;

public record WeatherDto(
        Double temperature,
        String description,
        String icon
) {}