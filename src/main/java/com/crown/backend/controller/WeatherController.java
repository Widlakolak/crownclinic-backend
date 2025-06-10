package com.crown.backend.controller;

import com.crown.backend.dto.WeatherDto;
import com.crown.backend.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping("/{city}")
    public ResponseEntity<WeatherDto> getWeather(@PathVariable String city) {
        try {
            WeatherDto dto = weatherService.getTodayWeather(city);
            return ResponseEntity.ok(dto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
