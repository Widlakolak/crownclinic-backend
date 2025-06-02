package com.crown.backend.mapper;

import com.crown.backend.domain.Message;
import com.crown.backend.domain.MessageAttachment;
import com.crown.backend.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    public MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getSubject(),
                message.getContent(),
                message.getSender().getFirstName() + " " + message.getSender().getLastName(),
                message.getRecipient().getFirstName() + " " + message.getRecipient().getLastName(),
                message.getSentAt(),
                message.getStatus(),
                message.getAttachments().stream().map(MessageAttachment::getFilename).toList()
        );
    }
}
