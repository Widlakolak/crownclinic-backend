package com.crown.backend.dto;

import com.crown.backend.domain.Message;

import java.time.LocalDateTime;
import java.util.List;

public record MessageResponseDto(
        Long id,
        String subject,
        String content,
        String senderName,
        String recipientName,
        LocalDateTime sentAt,
        Message.MessageStatus status,
        List<String> attachmentNames
) {}