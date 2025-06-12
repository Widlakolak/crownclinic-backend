package com.crown.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String notes;

    private String googleEventId;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private User doctor;

    public enum Status {
        SCHEDULED,
        CANCELLED,
        COMPLETED
    }
}
