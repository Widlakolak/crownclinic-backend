package com.crown.backend.service;

import com.crown.backend.domain.Message;
import com.crown.backend.dto.MessageResponseDto;
import com.crown.backend.mapper.MessageMapper;
import com.crown.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageThreadService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;

    public List<MessageResponseDto> getConversation(Long user1Id, Long user2Id) {
        List<Message> messages = messageRepository.findThreadBetween(user1Id, user2Id);
        return messages.stream()
                .map(messageMapper::toDto)
                .toList();
    }
}