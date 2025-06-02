package com.crown.backend.mapper;

import com.crown.backend.domain.User;
import com.crown.backend.domain.User.Role;
import com.crown.backend.dto.UserRequestDto;
import com.crown.backend.dto.UserResponseDto;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapDtoToEntity() {
        UserRequestDto userDto = new UserRequestDto(
                "Jan",
                "Kowalski",
                "jan@kowalski.com",
                "999111222",
                "calendar-id-123",
                Role.RECEPTIONIST
        );

        User entity = userMapper.toEntity(userDto);

        assertNotNull(entity);
        assertEquals("Jan", entity.getFirstName());
        assertEquals("Kowalski", entity.getLastName());
        assertEquals("999111222", entity.getPhone());
        assertEquals("jan@kowalski.com", entity.getEmail());
        assertEquals(Role.RECEPTIONIST, entity.getRole());
    }

    @Test
    void shouldMapUserToDto() {
        User user = User.builder().id(1L).firstName("Jan").lastName("Kowalski").build();
        UserResponseDto dto = userMapper.toDto(user);
        assertEquals("Jan", dto.firstName());
        assertEquals("Kowalski", dto.lastName());
    }
}