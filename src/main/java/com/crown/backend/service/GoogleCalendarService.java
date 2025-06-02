package com.crown.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

@Slf4j
@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "CrownClinic";
    private static final String CREDENTIALS_PATH = "src/main/resources/credentials.json";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Calendar getCalendarService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_PATH))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    public String createEvent(String calendarId, String summary, String description, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        try {
            Event event = new Event()
                    .setSummary(summary)
                    .setDescription(description);

            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDateTime.toInstant().toEpochMilli()))
                    .setTimeZone(ZoneId.systemDefault().toString());
            event.setStart(start);

            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(endDateTime.toInstant().toEpochMilli()))
                    .setTimeZone(ZoneId.systemDefault().toString());
            event.setEnd(end);

            Event created = getCalendarService().events().insert(calendarId, event).execute();
            log.info("Utworzono wydarzenie w Google Calendar: {}", created.getHtmlLink());
            return created.getId();
        } catch (Exception e) {
            log.error("Błąd przy tworzeniu wydarzenia: {}", e.getMessage());
            throw new RuntimeException("Nie udało się utworzyć wydarzenia w Google Calendar", e);
        }
    }

    public void updateEvent(String calendarId, String eventId, String newSummary, String newDescription, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        try {
            Event event = getCalendarService().events().get(calendarId, eventId).execute();

            event.setSummary(newSummary);
            event.setDescription(newDescription);

            event.setStart(new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDateTime.toInstant().toEpochMilli()))
                    .setTimeZone(ZoneId.systemDefault().toString()));

            event.setEnd(new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(endDateTime.toInstant().toEpochMilli()))
                    .setTimeZone(ZoneId.systemDefault().toString()));

            getCalendarService().events().update(calendarId, eventId, event).execute();
            log.info("Zaktualizowano wydarzenie w Google Calendar: {}", eventId);
        } catch (Exception e) {
            log.error("Błąd przy aktualizacji wydarzenia: {}", e.getMessage());
            throw new RuntimeException("Nie udało się zaktualizować wydarzenia w Google Calendar", e);
        }
    }

    public void deleteEvent(String calendarId, String eventId) {
        try {
            getCalendarService().events().delete(calendarId, eventId).execute();
            log.info("Usunięto wydarzenie z Google Calendar: {}", eventId);
        } catch (Exception e) {
            log.error("Błąd przy usuwaniu wydarzenia: {}", e.getMessage());
            throw new RuntimeException("Nie udało się usunąć wydarzenia z Google Calendar", e);
        }
    }
}
