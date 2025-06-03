package com.crown.backend.service;

import com.crown.backend.domain.Attachment;
import com.crown.backend.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public List<Attachment> saveAttachments(MultipartFile[] files, Attachment.AttachmentType type) {
        return Stream.of(files)
                .map(file -> {
                    try {
                        return Attachment.builder()
                                .filename(file.getOriginalFilename())
                                .contentType(file.getContentType())
                                .data(file.getBytes())
                                .attachmentType(type)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("Błąd pliku: " + file.getOriginalFilename());
                    }
                }).map(attachmentRepository::save)
                .toList();
    }
}
