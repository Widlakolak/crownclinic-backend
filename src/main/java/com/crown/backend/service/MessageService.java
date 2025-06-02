package com.crown.backend.service;

import com.crown.backend.domain.Conversation;
import com.crown.backend.domain.Message;
import com.crown.backend.domain.User;
import com.crown.backend.dto.MessageRequestDto;
import com.crown.backend.dto.MessageResponseDto;
import com.crown.backend.mapper.MessageMapper;
import com.crown.backend.repository.ConversationRepository;
import com.crown.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private final ConversationRepository conversationRepository;

    public List<MessageResponseDto> sendMessage(MessageRequestDto dto, MultipartFile[] files) {
        User sender = userService.findById(dto.senderId())
                .orElseThrow(() -> new IllegalArgumentException("Brak nadawcy"));

        return dto.recipientIds().stream()
                .map(recipientId -> createAndSaveMessage(sender, recipientId, dto.subject(), dto.content()))
                .collect(Collectors.toList());
    }

    private MessageResponseDto createAndSaveMessage(User sender, Long recipientId, String subject, String content) {
        User recipient = userService.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Brak odbiorcy o ID: " + recipientId));

        Conversation conversation = findOrCreateConversation(sender, recipient);

        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .subject(resolveThreadSubject(sender, recipient, subject))
                .content(content)
                .sentAt(LocalDateTime.now())
                .status(Message.MessageStatus.UNREAD)
                .conversation(conversation)
                .build();

        return messageMapper.toDto(messageRepository.save(message));
    }

    private Conversation findOrCreateConversation(User participant1, User participant2) {
        return conversationRepository
                .findByParticipant1AndParticipant2(participant1, participant2)
                .or(() -> conversationRepository.findByParticipant1AndParticipant2(participant2, participant1))
                .orElseGet(() -> conversationRepository.save(
                        Conversation.builder()
                                .participant1(participant1)
                                .participant2(participant2)
                                .build()));
    }


    private String resolveThreadSubject(User sender, User recipient, String baseSubject) {
        List<Message> existing = messageRepository.findThreadBetween(sender.getId(), recipient.getId());
        if (!existing.isEmpty()) {
            return existing.get(0).getSubject();
        }
        return baseSubject;
    }

    public List<MessageResponseDto> getInbox(Long recipientId) {
        return messageRepository.findByRecipientId(recipientId).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    public List<MessageResponseDto> getSent(Long senderId) {
        return messageRepository.findBySenderId(senderId).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    public void markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wiadomości"));
        message.setStatus(Message.MessageStatus.READ);
        messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    public void sendToMultiple(Long senderId, List<Long> recipientIds, String subject, String content) {
        User sender = userService.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Brak nadawcy"));

        for (Long recipientId : recipientIds) {
            User recipient = userService.findById(recipientId)
                    .orElseThrow(() -> new IllegalArgumentException("Brak odbiorcy"));

            Message msg = Message.builder()
                    .sender(sender)
                    .recipient(recipient)
                    .subject(subject)
                    .content(content)
                    .sentAt(LocalDateTime.now())
                    .status(Message.MessageStatus.UNREAD)
                    .build();

            messageRepository.save(msg);
        }
    }

    // TODO: Do tablic — patrz kolejne klasy
}