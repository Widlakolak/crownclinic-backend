package com.crown.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author;

    private String content;

    private LocalDateTime postedAt;

    @ManyToOne
    private PublicBoard publicBoard;

    @ManyToOne
    private PrivateBoard privateBoard;
}
