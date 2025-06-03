package com.crown.backend.mapper;

import com.crown.backend.domain.Message;
import com.crown.backend.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final AttachmentMapper attachmentMapper;

    public MessageResponseDto toDto(Message message) {
        String senderName = message.getSender().getFirstName() + " " + message.getSender().getLastName();

        List<String> recipientNames = message.getConversation().getParticipants().stream()
                .filter(user -> !user.equals(message.getSender()))
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList());

        return new MessageResponseDto(
                message.getId(),
                message.getSubject(),
                message.getContent(),
                senderName,
                recipientNames,
                message.getSentAt(),
                message.getStatus(),
                message.getAttachments().stream()
                        .map(attachmentMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}
