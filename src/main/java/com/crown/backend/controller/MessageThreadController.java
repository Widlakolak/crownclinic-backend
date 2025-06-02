package com.crown.backend.controller;

import com.crown.backend.dto.MessageThreadDto;
import com.crown.backend.service.MessageThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/threads")
@RequiredArgsConstructor
public class MessageThreadController {

    private final MessageThreadService threadService;

    @PostMapping
    public ResponseEntity<MessageThreadDto> startThread(
            @RequestBody MessageThreadDto threadDto) {
        return ResponseEntity.ok(threadService.createThread(threadDto));
    }

    @GetMapping("/{userId}")
    public List<MessageThreadDto> getUserThreads(@PathVariable Long userId) {
        return threadService.getThreadsByUser(userId);
    }
}
