package com.crown.backend.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "CrownClinic";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Calendar getCalendarService(String accessToken) throws GeneralSecurityException, IOException {
        HttpRequestInitializer requestInitializer = request -> request.getHeaders()
                .setAuthorization("Bearer " + accessToken);

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                requestInitializer
        ).setApplicationName(APPLICATION_NAME).build();
    }

    public String createEvent(String calendarId, String accessToken, String summary, String description,
                              ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        try {
            Calendar calendar = getCalendarService(accessToken);

            Event event = new Event()
                    .setSummary(summary)
                    .setDescription(description)
                    .setStart(new EventDateTime()
                            .setDateTime(new com.google.api.client.util.DateTime(startDateTime.toInstant().toEpochMilli()))
                            .setTimeZone(ZoneId.systemDefault().toString()))
                    .setEnd(new EventDateTime()
                            .setDateTime(new com.google.api.client.util.DateTime(endDateTime.toInstant().toEpochMilli()))
                            .setTimeZone(ZoneId.systemDefault().toString()));

            Event created = calendar.events().insert(calendarId, event).execute();
            log.info("Utworzono wydarzenie: {}", created.getHtmlLink());
            return created.getId();
        } catch (Exception e) {
            log.error("Błąd: {}", e.getMessage());
            throw new RuntimeException("Błąd przy tworzeniu wydarzenia", e);
        }
    }

    public void updateEvent(String calendarId, String accessToken, String eventId, String newSummary,
                            String newDescription, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        try {
            Calendar calendar = getCalendarService(accessToken);

            Event event = calendar.events().get(calendarId, eventId).execute();

            event.setSummary(newSummary);
            event.setDescription(newDescription);
            event.setStart(new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDateTime.toInstant().toEpochMilli()))
                    .setTimeZone(ZoneId.systemDefault().toString()));
            event.setEnd(new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(endDateTime.toInstant().toEpochMilli()))
                    .setTimeZone(ZoneId.systemDefault().toString()));

            calendar.events().update(calendarId, eventId, event).execute();
            log.info("Zaktualizowano wydarzenie w Google Calendar: {}", eventId);
        } catch (Exception e) {
            log.error("Błąd przy aktualizacji wydarzenia: {}", e.getMessage());
            throw new RuntimeException("Nie udało się zaktualizować wydarzenia w Google Calendar", e);
        }
    }

    public void deleteEvent(String calendarId, String accessToken, String eventId) {
        try {
            Calendar calendar = getCalendarService(accessToken);
            calendar.events().delete(calendarId, eventId).execute();
            log.info("Usunięto wydarzenie z Google Calendar: {}", eventId);
        } catch (Exception e) {
            log.error("Błąd przy usuwaniu wydarzenia: {}", e.getMessage());
            throw new RuntimeException("Nie udało się usunąć wydarzenia z Google Calendar", e);
        }
    }
}
