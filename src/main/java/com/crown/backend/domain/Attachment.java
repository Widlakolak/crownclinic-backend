package com.crown.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String contentType;

    @Lob
    private byte[] data;

    @Enumerated(EnumType.STRING)
    private AttachmentType attachmentType;

    public enum AttachmentType {
        MESSAGE,
        MEDICAL_RECORD,
        PATIENT
    }

    @ManyToOne
    private Message message;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private MedicalRecord medicalRecord;
}