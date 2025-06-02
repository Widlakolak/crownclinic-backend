package com.crown.backend.mapper;

import com.crown.backend.domain.Attachment;
import com.crown.backend.domain.Conversation;
import com.crown.backend.domain.Message;
import com.crown.backend.domain.User;
import com.crown.backend.dto.MessageResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    private final MessageMapper mapper = new MessageMapper();

    @Test
    void shouldMapMessageToDto() {
        // given
        User sender = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .build();

        User recipient = User.builder()
                .id(2L)
                .firstName("Anna")
                .lastName("Kowalska")
                .build();

        Attachment attachment1 = Attachment.builder()
                .filename("test1.pdf")
                .build();

        Attachment attachment2 = Attachment.builder()
                .filename("zdjecie.png")
                .build();

        Conversation conversation = Conversation.builder()
                .participants(List.of(sender, recipient))
                .build();

        Message message = Message.builder()
                .id(10L)
                .sender(sender)
                .subject("Testowa wiadomość")
                .content("To jest treść testowa.")
                .sentAt(LocalDateTime.of(2025, 6, 1, 10, 0))
                .status(Message.MessageStatus.UNREAD)
                .attachments(List.of(attachment1, attachment2))
                .conversation(conversation)
                .build();

        // when
        MessageResponseDto dto = mapper.toDto(message);

        // then
        assertEquals(10L, dto.id());
        assertEquals("Testowa wiadomość", dto.subject());
        assertEquals("To jest treść testowa.", dto.content());
        assertEquals("John Smith", dto.senderName());
        assertEquals("Anna Kowalska", dto.recipientName());
        assertEquals(Message.MessageStatus.UNREAD, dto.status());
        assertEquals(2, dto.attachmentNames().size());
        assertTrue(dto.attachmentNames().contains("test1.pdf"));
        assertTrue(dto.attachmentNames().contains("zdjecie.png"));
    }

    @Test
    void shouldMapGroupMessageToDto() {
        // given
        User sender = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .build();

        User recipient1 = User.builder()
                .id(2L)
                .firstName("Anna")
                .lastName("Kowalska")
                .build();

        User recipient2 = User.builder()
                .id(3L)
                .firstName("Kamil")
                .lastName("Nowak")
                .build();

        Attachment attachment = Attachment.builder()
                .filename("doc1.pdf")
                .build();

        Conversation conversation = Conversation.builder()
                .participants(List.of(sender, recipient1, recipient2))
                .build();

        Message message = Message.builder()
                .id(20L)
                .sender(sender)
                .subject("Grupowa wiadomość")
                .content("Hej wszystkim!")
                .sentAt(LocalDateTime.of(2025, 6, 2, 15, 0))
                .status(Message.MessageStatus.UNREAD)
                .attachments(List.of(attachment))
                .conversation(conversation)
                .build();

        // when
        MessageResponseDto dto = mapper.toDto(message);

        // then
        assertEquals("John Smith", dto.senderName());
        assertEquals("Anna Kowalska, Kamil Nowak", dto.recipientName());
        assertEquals("Grupowa wiadomość", dto.subject());
        assertEquals("Hej wszystkim!", dto.content());
        assertEquals(1, dto.attachmentNames().size());
        assertTrue(dto.attachmentNames().contains("doc1.pdf"));
    }
}
