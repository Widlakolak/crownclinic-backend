package com.crown.backend.service;

import com.crown.backend.domain.Patient;
import com.crown.backend.domain.User;
import com.crown.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUser() {
        List<User> list = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(list);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnUserById() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.findById(1L);

        assertTrue(found.isPresent());
    }

    @Test
    void shouldSaveUser() {

        User user = User.builder()
                .firstName("Anna")
                .lastName("Nowak")
                .email("anna@example.com")
                .phone("123456789")
                .role(User.Role.DOCTOR)
                .build();

        when(userRepository.save(any())).thenReturn(user);

        User saved = userService.save(user);

        assertNotNull(saved);
        assertEquals("Anna", saved.getFirstName());
        assertEquals("Nowak", saved.getLastName());
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateUser() {
        User existing = User.builder().id(1L).firstName("Jan").lastName("Kowalski").build();
        User updated = User.builder().firstName("Adam").lastName("Nowak").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenReturn(existing);

        User result = userService.update(1L, updated);

        assertEquals("Adam", result.getFirstName());
    }

    @Test
    void shouldDeleteUser() {
        userService.delete(5L);
        verify(userRepository).deleteById(5L);
    }
}