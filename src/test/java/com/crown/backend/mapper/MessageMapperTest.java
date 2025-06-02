package com.crown.backend.mapper;

import com.crown.backend.domain.Message;
import com.crown.backend.domain.MessageAttachment;
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
                .firstName("John")
                .lastName("Smith")
                .build();

        User recipient = User.builder()
                .firstName("Anna")
                .lastName("Kowalska")
                .build();

        MessageAttachment attachment1 = MessageAttachment.builder()
                .filename("test1.pdf")
                .build();

        MessageAttachment attachment2 = MessageAttachment.builder()
                .filename("zdjecie.png")
                .build();

        Message message = Message.builder()
                .id(10L)
                .sender(sender)
                .recipient(recipient)
                .subject("Testowa wiadomość")
                .content("To jest treść testowa.")
                .sentAt(LocalDateTime.of(2025, 6, 1, 10, 0))
                .status(Message.MessageStatus.UNREAD)
                .attachments(List.of(attachment1, attachment2))
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
}
