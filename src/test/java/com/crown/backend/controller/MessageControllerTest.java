package com.crown.backend.controller;

import com.crown.backend.config.JwtFilter;
import com.crown.backend.domain.Attachment;
import com.crown.backend.dto.AttachmentResponseDto;
import com.crown.backend.dto.MassMessageRequestDto;
import com.crown.backend.dto.MessageRequestDto;
import com.crown.backend.dto.MessageResponseDto;
import com.crown.backend.domain.Message;
import com.crown.backend.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MessageController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { JwtFilter.class })
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    void shouldSendMessage() throws Exception {
        MessageRequestDto requestDto = new MessageRequestDto(1L, List.of(2L), "Temat", "Treść wiadomości");
        MockMultipartFile file = new MockMultipartFile("attachments", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        MockMultipartFile messagePart = new MockMultipartFile("message", "", "application/json", objectMapper.writeValueAsBytes(requestDto));

        MessageResponseDto responseDto = new MessageResponseDto(1L, "Temat", "Treść wiadomości", "Dr A", List.of("Pacjent X"), LocalDateTime.now(), Message.MessageStatus.UNREAD, List.of(new AttachmentResponseDto(1L, "test.txt", "text/plain", Attachment.AttachmentType.MESSAGE))
        );

        Mockito.when(messageService.sendMessage(any(), any())).thenReturn(List.of(responseDto));

        mockMvc.perform(multipart("/api/messages")
                        .file(messagePart)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldSendMassMessage() throws Exception {
        MassMessageRequestDto dto = new MassMessageRequestDto(1L, List.of(2L), "Temat", "Treść");
        MockMultipartFile messagePart = new MockMultipartFile("message", "", "application/json", objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(multipart("/api/messages/broadcast")
                        .file(messagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetInbox() throws Exception {
        MessageResponseDto message = new MessageResponseDto(1L, "Temat", "Treść", "Dr A", List.of("Pacjent X"), LocalDateTime.now(), Message.MessageStatus.UNREAD, List.of());
        Mockito.when(messageService.getInbox(2L)).thenReturn(List.of(message));

        mockMvc.perform(get("/api/messages/inbox/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldGetSentMessages() throws Exception {
        MessageResponseDto message = new MessageResponseDto(1L, "Temat", "Treść", "Dr A", List.of("Pacjent X"), LocalDateTime.now(), Message.MessageStatus.UNREAD, List.of());
        Mockito.when(messageService.getSent(1L)).thenReturn(List.of(message));

        mockMvc.perform(get("/api/messages/sent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldMarkMessageAsRead() throws Exception {
        mockMvc.perform(patch("/api/messages/5/read"))
                .andExpect(status().isNoContent());
        Mockito.verify(messageService).markAsRead(5L);
    }

    @Test
    void shouldDeleteMessage() throws Exception {
        mockMvc.perform(delete("/api/messages/10"))
                .andExpect(status().isNoContent());
        Mockito.verify(messageService).deleteMessage(10L);
    }
}
