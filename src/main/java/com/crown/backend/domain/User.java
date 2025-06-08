package com.crown.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String googleCalendarId;

    private String googleAccessToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        DOCTOR,
        RECEPTIONIST
    }

    private String password;
}
