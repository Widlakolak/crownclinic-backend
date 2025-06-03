package com.crown.backend.controller;

import com.crown.backend.dto.MassMessageRequestDto;
import com.crown.backend.dto.MessageRequestDto;
import com.crown.backend.dto.MessageResponseDto;
import com.crown.backend.service.MessageService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<MessageResponseDto>> sendMessage(
            @Valid @RequestPart("message") MessageRequestDto dto,
            @RequestPart(name = "attachments", required = false) MultipartFile[] files) {

        List<MessageResponseDto> sentMessages = messageService.sendMessage(dto, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(sentMessages);
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Void> sendMassMessage(
            @Valid @RequestPart("message") MassMessageRequestDto dto,
            @RequestPart(name = "attachments", required = false) MultipartFile[] files) {

        messageService.sendMassMessage(dto.senderId(), dto.recipientIds(), dto.subject(), dto.content(), files);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inbox/{recipientId}")
    public ResponseEntity<List<MessageResponseDto>> getInbox(@PathVariable Long recipientId) {
        List<MessageResponseDto> inbox = messageService.getInbox(recipientId);
        return ResponseEntity.ok(inbox);
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<MessageResponseDto>> getSent(@PathVariable Long senderId) {
        List<MessageResponseDto> sent = messageService.getSent(senderId);
        return ResponseEntity.ok(sent);
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
}
