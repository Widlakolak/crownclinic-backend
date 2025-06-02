package com.crown.backend.mapper;

import com.crown.backend.domain.Attachment;
import com.crown.backend.domain.Message;
import com.crown.backend.domain.User;
import com.crown.backend.dto.MessageResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    public MessageResponseDto toDto(Message message) {
        String senderName = message.getSender().getFirstName() + " " + message.getSender().getLastName();

        List<String> recipientNames = message.getConversation().getParticipants().stream()
                .filter(user -> !user.getId().equals(message.getSender().getId()))
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList());

        List<String> attachmentNames = message.getAttachments().stream()
                .map(Attachment::getFilename)
                .toList();

        return new MessageResponseDto(
                message.getId(),
                message.getSubject(),
                message.getContent(),
                senderName,
                recipientNames,
                message.getSentAt(),
                message.getStatus(),
                attachmentNames
        );
    }
}
