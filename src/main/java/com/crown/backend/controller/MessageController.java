package com.crown.backend.controller;

import com.crown.backend.dto.MessageRequestDto;
import com.crown.backend.dto.MessageResponseDto;
import com.crown.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(
            @RequestPart("message") MessageRequestDto dto,
            @RequestPart("attachments") MultipartFile[] files) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.sendMessage(dto, files));
    }

    @GetMapping("/inbox/{recipientId}")
    public List<MessageResponseDto> getInbox(@PathVariable Long recipientId) {
        return messageService.getInbox(recipientId);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sent/{senderId}")
    public List<MessageResponseDto> getSent(@PathVariable Long senderId) {
        return messageService.getSent(senderId);
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Void> sendMassMessage(
            @RequestParam Long senderId,
            @RequestParam List<Long> recipientIds,
            @RequestParam String subject,
            @RequestParam String content,
            @RequestPart("attachments") MultipartFile[] files) {

        messageService.sendMassMessage(senderId, recipientIds, subject, content, files);
        return ResponseEntity.ok().build();
    }
}
