package com.crown.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String contentType;

    @Lob
    private byte[] data;

    @ManyToOne
    private MedicalRecord medicalRecord;

    @ManyToOne
    private Message message;

    @ManyToOne
    private Patient patient;
}