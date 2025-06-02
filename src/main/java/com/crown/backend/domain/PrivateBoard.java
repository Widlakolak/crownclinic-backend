package com.crown.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "privateBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMessage> messages = new ArrayList<>();
}
