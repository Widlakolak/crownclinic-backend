package com.crown.backend.repository;

import com.crown.backend.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByAttachmentType(Attachment.AttachmentType type);
    List<Attachment> findByPatientId(Long patientId);
}
