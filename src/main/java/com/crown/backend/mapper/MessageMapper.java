package com.crown.backend.mapper;

import com.crown.backend.domain.Attachment;
import com.crown.backend.domain.Message;
import com.crown.backend.domain.User;
import com.crown.backend.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    public MessageResponseDto toDto(Message message) {
        String senderName = formatName(message.getSender());

        String recipientNames = message.getConversation().getParticipants().stream()
                .filter(user -> !user.getId().equals(message.getSender().getId()))
                .map(this::formatName)
                .collect(Collectors.joining(", "));


        return new MessageResponseDto(
                message.getId(),
                message.getSubject(),
                message.getContent(),
                senderName,
                recipientNames,
                message.getSentAt(),
                message.getStatus(),
                message.getAttachments().stream()
                        .map(Attachment::getFilename)
                        .toList()
        );
    }

    private String formatName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
