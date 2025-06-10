package com.crown.backend.service;

import com.crown.backend.dto.WeatherDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WeatherService {
    @Value("${openweathermap.key}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();
    private static final String URL =
            "https://api.openweathermap.org/data/2.5/weather?q={city}&units=metric&appid={key}";

    public WeatherDto getTodayWeather(String city) throws JsonProcessingException {
        String url = "https://api.openweathermap.org/data/2.5/weather?q={city}&units=metric&appid={key}";
        String json = rest.getForObject(url, String.class, Map.of("city", city, "key", apiKey));
        JsonNode root = new ObjectMapper().readTree(json);
        double temperature = root.path("main").path("temp").asDouble();
        String description = root.path("weather").get(0).path("description").asText();
        String icon = root.path("weather").get(0).path("icon").asText();
        return new WeatherDto(temperature, description, icon);
    }
}