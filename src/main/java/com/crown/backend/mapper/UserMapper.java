package com.crown.backend.mapper;

import com.crown.backend.domain.User;
import com.crown.backend.dto.UserRequestDto;
import com.crown.backend.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto dto) {
        return User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phone(dto.phone())
                .googleCalendarId(dto.googleCalendarId())
                .role(dto.role())
                .build();
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getGoogleCalendarId(),
                user.getRole()
        );
    }
}
