package com.crown.backend.controller;

import com.crown.backend.domain.User;
import com.crown.backend.domain.User.Role;
import com.crown.backend.dto.UserRequestDto;
import com.crown.backend.dto.UserResponseDto;
import com.crown.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserResponseDto dto = new UserResponseDto(
                1L,
                "John",
                "Doe",
                "john@example.com",
                "123456789",
                null,
                Role.DOCTOR
        );

        Mockito.when(userService.findAll()).thenReturn(List.of(
                User.builder()
                        .id(dto.id())
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .phone(dto.phone())
                        .googleCalendarId(dto.googleCalendarId())
                        .role(dto.role())
                        .build()
        ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("123456789")
                .googleCalendarId(null)
                .role(Role.DOCTOR)
                .build();

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "John",
                "Doe",
                "john@example.com",
                "123456789",
                null,
                Role.DOCTOR
        );

        User saved = User.builder()
                .id(1L)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .googleCalendarId(request.googleCalendarId())
                .role(request.role())
                .build();

        Mockito.when(userService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "Jan",
                "Kowalski",
                "jan@kowalski.com",
                "999111222",
                null,
                Role.RECEPTIONIST
        );

        User updated = User.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.com")
                .phone("999111222")
                .googleCalendarId(null)
                .role(Role.RECEPTIONIST)
                .build();

        Mockito.when(userService.update(eq(1L), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).delete(1L);
    }
}
